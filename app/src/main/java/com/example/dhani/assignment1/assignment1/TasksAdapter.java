package com.example.dhani.assignment1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dhani on 11/09/2017.
 */

public class TasksAdapter extends ArrayAdapter
{
    private ArrayList dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder
    {
        TextView txtName;
        CheckBox checkBox;
    }

    public TasksAdapter(ArrayList data, Context context)
    {
        super(context, R.layout.task_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public TasksDataModel getItem(int position) {
        return (TasksDataModel)dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        TasksAdapter.ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new TasksAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (TasksAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        TasksDataModel item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}
