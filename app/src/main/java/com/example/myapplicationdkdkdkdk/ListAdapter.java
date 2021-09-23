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

    private ArrayList<String> array;

    private ViewHolder viewHolder;

    public ListAdapter(Context context,ArrayList<String> array){
        this.context=context;
        this.array=array;
    }
    @Override

    public int getCount() {

        return array.size();
    }



    @Override

    public Object getItem(int position) {

        return array.get(position);

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

        viewHolder.txt_name.setText(array.get(position));



        return convertView;

    }

    public class ViewHolder {

        private TextView txt_name;

        public ViewHolder(View convertView) {

            txt_name = (TextView) convertView.findViewById(R.id.list_answer);

        }

    }
}
