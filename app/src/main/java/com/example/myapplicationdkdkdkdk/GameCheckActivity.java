package com.example.myapplicationdkdkdkdk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

/**
 * @author Abel Weldaregay
 */
public class GameCheckActivity extends AppCompatActivity {

    public static final String TAG = "GameCheckActivity";
    public static final String FILE_NAME = "TaskList";
    ArrayList<Task> tasks = new ArrayList<>();
    /**
     *
     * @param view
     */
    public void addTaskClicked(View view) {

        EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
        EditText taskDescriptionEditText = (EditText) findViewById(R.id.taskDescEditText);

        String taskTitle = taskTitleEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();

        if(taskTitle.isEmpty()) {
            Toast.makeText(GameCheckActivity.this, "Task Title cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (taskDescription.isEmpty()) {
            Toast.makeText(GameCheckActivity.this, "Task Description cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
        {
            taskTitleEditText.setText("");
            taskDescriptionEditText.setText("");
            Task newTask = new Task(taskTitle, taskDescription);
            tasks.add(newTask);
            saveData();
            TaskAdapter adapter = new TaskAdapter(GameCheckActivity.this, R.layout.view, tasks);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);

        }

    }

    /**
     * Removes task to task list when checkbox is clicked
     * @param view
     */
    public void checkBoxClicked(View view) {

        int position = (Integer) view.getTag();

        tasks.remove(position);
        saveData();
        TaskAdapter adapter = new TaskAdapter(this, R.layout.view, tasks);
        ListView listView = findViewById(R.id.listView);
        listView.animate();
        listView.setAdapter(adapter);
        Toast.makeText(GameCheckActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_check);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar);
        Log.d(TAG, "onCreate: started");

        final ListView listView = findViewById(R.id.listView);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        loadData();
        final TaskAdapter adapter = new TaskAdapter(this, R.layout.view, tasks);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                tasks.remove(position);
                saveData();

                TaskAdapter adapter1 = new TaskAdapter(GameCheckActivity.this, R.layout.view, tasks);
                listView.setAdapter(adapter);

                Toast.makeText(GameCheckActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();



                return false;
            }
        });


    }


    /**
     * Saves tasks to a txt file
     */
    private void saveData(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(tasks);
        editor.putString("task list", json);
        editor.apply();

    }

    /**
     * Reads data from a text file
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken< ArrayList<Task> >() {}.getType();
        tasks = gson.fromJson(json, type);

        if(tasks == null) {
            tasks = new ArrayList<Task>();
        }

    }




}