package com.example.sproje;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SubGoalsListAdapter extends ArrayAdapter<String> {

    public SubGoalsListAdapter(Context context, List<String> subGoalsList) {
        super(context, 0, subGoalsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Her bir alt hedefin görünümünü oluşturma
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listsub, parent, false);
        }

        // TextView'e alt hedefi yerleştirme
        TextView textViewSubGoal = convertView.findViewById(R.id.textViewSubGoal);
        String subGoal = getItem(position);
        textViewSubGoal.setText(subGoal);

        return convertView;
    }
}

