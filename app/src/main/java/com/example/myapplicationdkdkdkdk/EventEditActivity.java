package com.example.myapplicationdkdkdkdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;

import me.jlurena.revolvingweekview.DayTime;
import me.jlurena.revolvingweekview.WeekViewEvent;

import static android.os.Build.ID;
import static com.example.myapplicationdkdkdkdk.MainActivity.randomColor;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;

    public int startTime;
    public int endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();


    }

    private void initWidgets()
    {

        eventNameET = findViewById(R.id.eventNameET);

    }

    public void saveEventAction(View view)
    {
        TimePicker start = (TimePicker) findViewById(R.id.datePicker1);
        TimePicker end = (TimePicker) findViewById(R.id.datePicker2);

        startTime = start.getHour();
        endTime =  end.getHour();

        //WeekViewEvent event = new WeekViewEvent(ID+1, eventNameET.getText().toString(), startTime, endTime);
        //event.setColor(randomColor());
        //events.add(event);

        finish();
    }
}