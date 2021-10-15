package com.example.myapplicationdkdkdkdk;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

public class GameCheckActivity extends Activity implements OnClickListener{

    public LinkedHashMap<String, Parent> mygroup = new LinkedHashMap<String, Parent>();
    public ArrayList<Parent> deptList = new ArrayList<Parent>();

    private GameCheckAdapter listAdapter;
    public ExpandableListView myList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_check);

        Spinner spinner = (Spinner) findViewById(R.id.group);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        loadData();


        myList = (ExpandableListView) findViewById(R.id.myList);

        listAdapter = new GameCheckAdapter(GameCheckActivity.this, deptList);

        myList.setAdapter(listAdapter);

        //expand all Groups
        expandAll();

        //add new item to the List
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        //go back main activity
        Button done= (Button) findViewById(R.id.done);
        done.setOnClickListener(this);


    }

    public void onClick(View v) {

        switch (v.getId()) {

            //add entry to the List
            case R.id.add:

                Spinner spinner = (Spinner) findViewById(R.id.group);
                String group = spinner.getSelectedItem().toString();
                EditText editText = (EditText) findViewById(R.id.task);
                String task = editText.getText().toString();
                editText.setText("");
                //add a new item to the list
                int groupPosition = addTask(group,task);
                //notify the list so that changes can take effect
                listAdapter.notifyDataSetChanged();

                collapseAll();
                //expand the group where item was just added
                myList.expandGroup(groupPosition);
                //set the current group to be selected so that it becomes visible
                myList.setSelectedGroup(groupPosition);
                break;

            case R.id.done:
                finish();
                break;
        }
    }

    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++)
        {
            myList.expandGroup(i);
        }
    }

    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++)
        {
            myList.collapseGroup(i);
        }
    }

    private void loadData(){
        addTask("월간","파판 레이드");

        addTask("주간","도전 가디언토벌");

        addTask("일간","카던돌기");


    }

    private int addTask(String group, String child){

        int groupPosition = 0;

        //check the hash map if the group already exists
        Parent Parent = mygroup.get(group);
        //add the group if doesn't exists
        if(Parent == null){
            Parent = new Parent();
            Parent.setName(group);
            mygroup.put(group, Parent);
            deptList.add(Parent);
        }

        //get the children for the group
        ArrayList<Child> taskList = Parent.childList();
        //size of the children list
        int listSize = taskList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        Child detailInfo = new Child();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(child);
        taskList.add(detailInfo);
        Parent.setChildnameList(taskList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(Parent);
        return groupPosition;
    }


    //set parents
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }
}


