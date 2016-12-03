package com.android.studentslist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.studentslist.R;
import com.android.studentslist.StudentsInfo;
import com.android.studentslist.adapters.ListStudentsAdapter;

public class ListViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("List View");
        ((ListView) getActivity().findViewById(R.id.list_view)).setAdapter(new ListStudentsAdapter(getActivity(), StudentsInfo.data));
    }


}
