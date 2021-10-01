package com.example.myapplicationdkdkdkdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import org.tensorflow.lite.examples.textclassification.client.Result;
import org.tensorflow.lite.examples.textclassification.client.TextClassificationClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    public static final int ADD = 1;
    public static final int EDIT = 2;

    int i,j;
    float max;
    int maxindex;
    float reset=0;

    private Button addBtn;
    private Button clearBtn;
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
        init();
    }

    private void init(){
        this.context = this;
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
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
        goBtn.setOnClickListener(this);
        recentBtn.setOnClickListener(this);

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EventEditActivity.class);
                i.putExtra("mode", EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i, EDIT);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                Intent i = new Intent(this,EventEditActivity.class);
                i.putExtra("mode", ADD);
                startActivityForResult(i, ADD);
                break;
            case R.id.clear_btn:
                timetable.removeAll();
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
            case ADD:
                if (resultCode == EventEditActivity.ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                }
                break;
            case EDIT:
                /** Edit -> Submit */
                if (resultCode == EventEditActivity.EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                }
                /** Edit -> Delete */
                else if (resultCode == EventEditActivity.DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }


    /** get json data from SharedPreferences and then restore the timetable */
    public void gameCheckAction(View view) {
        startActivity(new Intent(this, GameCheckActivity.class));
    }

    public void letsLSTM() {

        for( i=0; i<workList.size()+1;i++)
        {
            float confi=(float)((Math.random()*100)%100);
            confiList.add(confi);
            //confiList.add((float)68.5);
        }


        startActivity(new Intent(this,ShowContentActivity.class));



    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

