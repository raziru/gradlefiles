package com.example.myapplicationdkdkdkdk;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String> task;
    private ArrayList<String> work;
    private ArrayList<String> confi;
    private ViewHolder viewHolder;

    public ListAdapter(Context context,ArrayList<String> task, ArrayList<String> work,ArrayList<String> confi){
        this.context=context;
        this.work=work;
        this.task=task;
        this.confi=confi;
    }
    @Override

    public int getCount() {

        return task.size();
    }



    @Override

    public Object getItem(int position) {

        return task.get(position);

    }



    @Override

    public long getItemId(int position) {

        return position;

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }



        // View에 Data 세팅

        viewHolder.txt_name.setText(task.get(position));



        return convertView;

    }



    public class ViewHolder {

        private TextView txt_name;



        public ViewHolder(View convertView) {

            txt_name = (TextView) convertView.findViewById(R.id.taskname);

        }

    }
}
