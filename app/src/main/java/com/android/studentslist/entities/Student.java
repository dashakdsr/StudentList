package com.android.studentslist.entities;

import io.realm.RealmObject;

public class Student extends RealmObject {
    private String name, gPlusId, gitId;

    public Student() {
    }

    public Student(String name, String gPlusId, String gitId) {
        this.name = name;
        this.gPlusId = gPlusId;
        this.gitId = gitId;
    }

    public String getName() {
        return name;
    }

    public String getGPlusId() {
        return gPlusId;
    }

    public String getGitId() {
        return gitId;
    }
}
