package com.example.myapplicationdkdkdkdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowContentActivity extends AppCompatActivity {
    private ArrayList<String> array;
    int i;
    String temp;

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        mListView = (ListView) findViewById(R.id.list_mountain);



        // 데이터 생성

        array = new ArrayList<>();

        for(i=0;i<MainActivity.taskList.size();i++)
        {
        }



    }
}