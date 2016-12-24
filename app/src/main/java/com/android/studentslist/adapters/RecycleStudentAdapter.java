package com.android.studentslist.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.studentslist.R;
import com.android.studentslist.activities.GPlusActivity;
import com.android.studentslist.activities.GitActivity;
import com.android.studentslist.entities.Student;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class RecycleStudentAdapter extends RecyclerView.Adapter<RecycleStudentAdapter.StudentHolder> {
    private List<Student> students;
    private final Context parent_context;

    public RecycleStudentAdapter(RealmResults<Student> students, Context context) {
        this.students = students;
        this.parent_context = context;
    }

    public void setFilter(ArrayList<Student> newList) {
        students = new ArrayList<>();
        students.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new StudentHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
        holder.textView.setText(students.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        StudentHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.list_item_username);
            view.findViewById(R.id.list_item_git).setOnClickListener(this);
            view.findViewById(R.id.list_item_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Student student = students.get(getAdapterPosition());
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
}
