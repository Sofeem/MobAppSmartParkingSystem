package com.example.spmons;

class DataList{
    private String id;
    private String Status;

    // Parameterized constructor for Employee class
    public DataList(String id, String Status) {
        this.id = id;
        this.Status = Status;
    }

    // Creating getters for Employee class
    public String getid() {
        return id;
    }

    public String getStatus() {
        return Status;
    }
}