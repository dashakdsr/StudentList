package com.android.studentslist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.studentslist.R;
import com.android.studentslist.StudentsInfo;
import com.android.studentslist.adapters.RecycleStudentAdapter;
import com.android.studentslist.entities.Student;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RecycleViewFragment extends Fragment implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private Realm realm;
    private RealmResults<Student> students;
    RecycleStudentAdapter studentAdapter;

    RealmChangeListener<RealmResults<Student>> changeListener = new RealmChangeListener<RealmResults<Student>>() {
        @Override
        public void onChange(RealmResults<Student> element) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(new RecycleStudentAdapter(element, getActivity()));
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_recycle_view, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Recycle View");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(Arrays.asList(StudentsInfo.data));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable e) {
            }
        });
        realm.commitTransaction();

        final RealmResults<Student> students = realm.where(Student.class)
                .findAllAsync();
        students.addChangeListener(changeListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecycleStudentAdapter(students, getActivity()));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Student> newList = new ArrayList<>();

        for (Student student : students) {
            String name = student.getName().toLowerCase();
            if (name.contains(newText))
                newList.add(student);
        }
        studentAdapter.setFilter(newList);
        return true;    }
}
