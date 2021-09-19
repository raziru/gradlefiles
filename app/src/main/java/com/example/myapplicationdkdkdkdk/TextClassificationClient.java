/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplicationdkdkdkdk;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.metadata.MetadataExtractor;

/** Interface to load TfLite model and provide predictions. */
public class TextClassificationClient {
  private static final String TAG = "TextClassificationDemo";
  private static final String MODEL_PATH = "/mymodelLSTM.tflite";
  private static final String DIC_PATH = "text_classification_vocab.txt";
  private static final String LABEL_PATH = "text_classification_labels.txt";

  private static final int SENTENCE_LEN = 256;
  private static final String SIMPLE_SPACE_OR_PUNCTUATION = " |\\\\,|\\\\.|\\\\!|\\\\?|\\n";

  private static final String START = "<START>";
  private static final String PAD = "<PAD>";
  private static final String UNKNOWN = "<UNKNOWN>";

  private static final int MAX_RESULTS = 3;

  private final Context context;
  private final Map<String, Integer> dic = new HashMap<>();
  private final List<String> labels = new ArrayList<>();
  private Interpreter tflite;

  public static class Result {

    private final String id;
    private final String title;
    private final Float confidence;

    public Result(String id, String title, Float confidence) {
      this.id = id;
      this.title = title;
      this.confidence = confidence;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public Float getConfidence() {
      return confidence;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
      String resultString = "";

      if (id != null) {
        resultString += "[" + id + "] ";
      }

      if (title != null) {
        resultString += title + " ";
      }

      if (confidence != null) {
        resultString += String.format("(%.1f%%) ", confidence * 100.0f);
      }

      return resultString.trim();
    }
  };

  public TextClassificationClient(Context context) {
    this.context = context;
  }

  @WorkerThread
  public void load() {
    loadModel();
    loadDictionary();
    loadLabels();
  }

  @WorkerThread
  private synchronized void loadModel() {
    try {
      ByteBuffer buffer = loadModelFile(this.context.getAssets());
      tflite = new Interpreter(buffer);
      Log.v(TAG, "TFLite Model Loaded");

    } catch (IOException ex) {
      Log.v(TAG, ex.getMessage());
    }
  }

  @WorkerThread
  private synchronized void loadDictionary() {
    try {
      loadDictionaryFile(this.context.getAssets());
      Log.v(TAG, "Dictionary Loaded");
    } catch (IOException ex) {
      Log.v(TAG, ex.getMessage());
    }
  }

  @WorkerThread
  private synchronized void loadLabels() {
    try {
      loadLabelFile(this.context.getAssets());
      Log.v(TAG, "Labels Loaded");
    } catch (IOException ex) {
      Log.v(TAG, ex.getMessage());
    }
  }

  @WorkerThread
  private synchronized void unload(){
    tflite.close();
    dic.clear();
    labels.clear();
  }

  @WorkerThread
  public synchronized List<Result> classify(String text) {
    float[][] input = tokenizeInputText(text);

    Log.v(TAG, "Classifying with TFLite");

    float[][] output = new float[1][labels.size()];
    System.out.println("input inside classify in textclient" + Arrays.deepToString(input) + " and labels size is " + labels.size());
    System.out.println("Out put is " + Arrays.deepToString(output));
    tflite.run(input, output);

    PriorityQueue<Result> pq = new PriorityQueue<>(
            MAX_RESULTS, (lhs, rhs) -> Float.compare(rhs.getConfidence(), lhs.getConfidence()));
    for(int i = 0; i < labels.size(); i++) {
      pq.add(new Result("" + i, labels.get(i), output[0][i]));
    }

    final ArrayList<Result> results = new ArrayList<>();
    while (!pq.isEmpty()){
      results.add(pq.poll());
    }

    return results;
  }

  private static MappedByteBuffer loadModelFile(AssetManager assetManager) throws IOException {

    try(AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
      FileChannel fileChannel = inputStream.getChannel();
      long startOffset = fileDescriptor.getStartOffset();
      long declaredLength = fileDescriptor.getDeclaredLength();
      return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
  }

  private void loadLabelFile(AssetManager assetManager) throws IOException{
    try (InputStream ins = assetManager.open(LABEL_PATH);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins))){
      while (bufferedReader.ready()) {
        labels.add(bufferedReader.readLine());
      }
    }
  }

  private void loadDictionaryFile(AssetManager assetManager) throws IOException{
    try (InputStream ins = assetManager.open(DIC_PATH);
         BufferedReader reader = new BufferedReader(new InputStreamReader(ins))){
      while (reader.ready()){
        List<String> line = Arrays.asList(reader.readLine().split(" "));
        if (line.size() < 2){
          continue;
        }

        dic.put(line.get(0), Integer.parseInt(line.get(1)));
      }
    }
  }

  float[][] tokenizeInputText(String text) {

    float[] tmp = new float[SENTENCE_LEN];
    List<String> array = Arrays.asList(text.split(SIMPLE_SPACE_OR_PUNCTUATION));

    int index = 0;
    // Prepend <START> if it is in vocabulary file.
    if (dic.containsKey(START)) {
      tmp[index++] = dic.get(START);
    }

    for (String word : array) {
      if (index >= SENTENCE_LEN) {
        break;
      }
      tmp[index++] = dic.containsKey(word) ? dic.get(word) : (int) dic.get(UNKNOWN);
    }
    // Padding and wrapping.
    Arrays.fill(tmp, index, SENTENCE_LEN - 1, (int) dic.get(PAD));
    float[][] ans = {tmp};
    return ans;
  }

  Map<String, Integer> getDic() {
    return this.dic;
  }

  Interpreter getTflite() {
    return this.tflite;
  }

  List<String> getLabels(){
    return this.labels;
  }
}

