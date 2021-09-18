package com.example.myapplicationdkdkdkdk;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.jlurena.revolvingweekview.DayTime;
import me.jlurena.revolvingweekview.WeekView;
import me.jlurena.revolvingweekview.WeekViewEvent;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements WeekView.EventClickListener, WeekView.WeekViewLoader,
        WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekView.EmptyViewClickListener,
        WeekView.AddEventClickListener, WeekView.DropListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final Random random = new Random();
    protected WeekView mWeekView;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;


    public static @ColorInt
    int randomColor() {
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    protected String getEventTitle(DayTime time) {
        return String.format(Locale.getDefault(), "Event of %s %02d:%02d", time.getDay().getDisplayName(TextStyle
                .SHORT, Locale.getDefault()), time.getHour(), time.getMinute());
    }

    @Override
    public void onAddEventClicked(DayTime startTime, DayTime endTime) {
        startActivity(new Intent(this, EventEditActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView draggableView = findViewById(R.id.draggable_view);
        draggableView.setOnLongClickListener(new DragTapListener());

        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setWeekViewLoader(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set EmptyView Click Listener
        mWeekView.setEmptyViewClickListener(this);

        // Set AddEvent Click Listener
        mWeekView.setAddEventClickListener(this);

        // Set Drag and Drop Listener
        mWeekView.setDropListener(this);


        setupDateTimeInterpreter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDrop(View view, DayTime day) {
        Toast.makeText(this, "View dropped to " + day.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewClicked(DayTime day) {

        //Toast.makeText(this, "Empty view" + " clicked: " + getEventTitle(day), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(DayTime time) {
        //Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        new AlertDialog.Builder(this)
                .setTitle("삭제")
                .setMessage("삭제 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        event.setName("");
                        event.setColor(android.R.color.transparent);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }})
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter();
        switch (id) {
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                            getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                            getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                            getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                            getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                            getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                            getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                            getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                            getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                            getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public List<? extends WeekViewEvent> onWeekViewLoad() {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            DayTime startTime = new DayTime(LocalDateTime.now().plusHours(i * (random.nextInt(3) + 1)));
            DayTime endTime = new DayTime(startTime);
            endTime.addMinutes(random.nextInt(30) + 30);
            WeekViewEvent event = new WeekViewEvent("ID" + i, "Event " + i, startTime, endTime);
            event.setColor(randomColor());
            events.add(event);
        }
        return events;
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     */
    private void setupDateTimeInterpreter() {
        mWeekView.setDayTimeInterpreter(new WeekView.DayTimeInterpreter() {
            @Override
            public String interpretDay(int date) {
                return DayOfWeek.of(date).getDisplayName(TextStyle.SHORT, Locale.getDefault());
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                String strMinutes = String.format(Locale.getDefault(), "%02d", minutes);
                if (hour > 11) {
                    return (hour == 12 ? "12:" + strMinutes : (hour - 12) + ":" + strMinutes) + " PM";
                } else {
                    if (hour == 0) {
                        return "12:" + strMinutes + " AM";
                    } else {
                        return hour + ":" + strMinutes + " AM";
                    }
                }
            }
        });
    }

    public void gameCheckAction(View view) {
        startActivity(new Intent(this, GameCheckActivity.class));
    }

    private final class DragTapListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        }
    }

}