package com.example.myapplicationdkdkdkdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.myapplicationdkdkdkdk.MainActivity.confiList;
import static com.example.myapplicationdkdkdkdk.MainActivity.taskList;
import static com.example.myapplicationdkdkdkdk.MainActivity.workList;

public class ShowContentActivity extends AppCompatActivity {
    private ArrayList<String> array;
    int i;
    String temp;
    private ListAdapter mListAdapter;

    private ListView mListView;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getApplicationContext();
        setContentView(R.layout.activity_show_content);
        mListView = (ListView) findViewById(R.id.list_answer);



        // 데이터 생성

        array = new ArrayList<>();

        for(i=0; i< taskList.size(); i++)
        {
            array.add(String.format("게임업무 %s는 %s근처에 두면 좋을것 같아요 자신감:%5.5f",taskList.get(i),workList.get(i),confiList.get(i)));
        }

        mListAdapter = new ListAdapter(mContext, array);

        mListView.setAdapter(mListAdapter);





    }
    public void goback(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}