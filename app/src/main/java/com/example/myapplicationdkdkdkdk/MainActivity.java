package com.example.myapplicationdkdkdkdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import org.tensorflow.lite.examples.textclassification.client.Result;
import org.tensorflow.lite.examples.textclassification.client.TextClassificationClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    private Handler handler;

    int i,j;
    float max;
    int maxindex;
    float reset=0;

    private Button addBtn;
    private Button clearBtn;
    private Button saveBtn;
    private Button loadBtn;
    private Button goBtn;
    private Button recentBtn;
    private TextClassificationClient client;

    public static List<Float> confiList;
    public static List<Float> sortedconfiList;
    public static List<String> taskList;
    public static List<String> workList;

    private TimetableView timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new TextClassificationClient(getApplicationContext());
        handler = new Handler();
        init();
    }

    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
        saveBtn = findViewById(R.id.save_btn);
        loadBtn = findViewById(R.id.load_btn);
        goBtn = findViewById(R.id.go_btn);
        recentBtn=findViewById(R.id.recent_btn);
        taskList=new ArrayList<String>();
        workList=new ArrayList<String>();
        confiList=new ArrayList<Float>();
        sortedconfiList=new ArrayList<Float>();


        timetable = findViewById(R.id.timetable);
        timetable.setHeaderHighlight(1);
        timetable.setHeaderHighlight(2);
        timetable.setHeaderHighlight(3);
        timetable.setHeaderHighlight(4);
        timetable.setHeaderHighlight(5);
        initView();

    }

    private void initView(){
        addBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        goBtn.setOnClickListener(this);
        recentBtn.setOnClickListener(this);

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EventEditActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                Intent i = new Intent(this,EventEditActivity.class);
                i.putExtra("mode",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);
                break;
            case R.id.clear_btn:
                timetable.removeAll();
                break;
            case R.id.save_btn:
                saveByPreference(timetable.createSaveData());
                break;
            case R.id.load_btn:
                loadSavedData();
                break;
            case R.id.go_btn:
                letsLSTM();
                break;
            case R.id.recent_btn:
                SeeRecent();
                break;

        }
    }

    private void SeeRecent() {
        startActivity(new Intent(this,ShowContentActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == EventEditActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                }
                break;
            case REQUEST_EDIT:
                /** Edit -> Submit */
                if (resultCode == EventEditActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == EventEditActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }

    /** save timetableView's data to SharedPreferences in json format */
    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        Toast.makeText(this,"saved!",Toast.LENGTH_SHORT).show();
    }

    /** get json data from SharedPreferences and then restore the timetable */
    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable","");
        if(savedData == null && savedData.equals("")) return;
        timetable.load(savedData);
        Toast.makeText(this,"loaded!",Toast.LENGTH_SHORT).show();
    }
    public void gameCheckAction(View view) {
        startActivity(new Intent(this, GameCheckActivity.class));
    }

    public void letsLSTM() {

        for( i=0; i<workList.size();i++)
        {
            classify(workList.get(i));
        }

        max=0;
        maxindex=0;
        reset=0;
        for(i=0;i<taskList.size();i++)
        {
            for( j=0;j<workList.size();j++)
            {
                if(confiList.get(j)>=max)
                {
                    maxindex=j;
                }
            }
            sortedconfiList.add(confiList.get(maxindex));
            confiList.set(maxindex,reset);
        }

       // startActivity(new Intent(this,ShowContentActivity.class));



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void classify(final String text) {

        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);

                    // Show classification result on screen
                    showResult(results);
                });


    }

    private void showResult(final List<Result> results) {
        // Run on UI thread as we'll updating our app UI

        confiList.add(results.get(0).getConfidence());

    }



}

