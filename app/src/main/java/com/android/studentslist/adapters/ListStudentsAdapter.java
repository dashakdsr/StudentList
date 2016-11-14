package com.android.studentslist.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.studentslist.R;
import com.android.studentslist.activities.GPlusActivity;
import com.android.studentslist.activities.GitActivity;
import com.android.studentslist.entities.Student;

public class ListStudentsAdapter extends ArrayAdapter<Student> implements View.OnClickListener {

    private final Context parent_context;

    public ListStudentsAdapter(Context context, Student[] objects) {
        super(context, 0, objects);
        this.parent_context = context;
    }

    @Override
    public void onClick(View view) {
        Student student = getItem(((ListView) ((Activity) parent_context).findViewById(R.id.list_view)).getPositionForView(view));
        if (student != null) {
            switch (view.getId()) {
                case R.id.list_item_layout: {
                    Intent intent = new Intent(parent_context, GPlusActivity.class)
                            .setData(Uri.parse("https://plus.google.com").buildUpon().appendPath(student.getGPlusId()).build());
                    parent_context.startActivity(intent);
                    break;
                }
                case R.id.list_item_git: {
                    Intent intent = new Intent(parent_context, GitActivity.class)
                            .setData(Uri.parse("https://github.com").buildUpon().appendPath(student.getGitId()).build());
                    parent_context.startActivity(intent);
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
        }
        //noinspection ConstantConditions
        ((TextView) view.findViewById(R.id.list_item_username)).setText(getItem(position).getName());
        view.findViewById(R.id.list_item_layout).setOnClickListener(this);
        view.findViewById(R.id.list_item_git).setOnClickListener(this);
        return view;
    }
}
