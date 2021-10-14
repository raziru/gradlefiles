async function start() {
  const textarea = document.querySelector("textarea");
  const resultDiv = document.querySelector(".result");
  // Load the TFLite model.
  const model = await tfTask.NLClassification.CustomModel.TFLite.load({
    model:
    "https://storage.googleapis.com/tfweb/models/movie_review_sentiment_classification.tflite"
    //"https://storage.googleapis.com/tensorflow_mine/mymodelLSTM.tflite"
  });

  document.querySelector(".btn").addEventListener("click", async () => {
    // Get the classification result for the entered text
    var temp=textarea.value;
    const result = await model.predict(temp);
    // Show the results.
    resultDiv.textContent = result.classes
      .map((c) => `${c.className}: ${c.score.toFixed(3)}`)
      .join(", ");
  });
}
start();
