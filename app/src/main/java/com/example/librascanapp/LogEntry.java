package com.example.librascanapp;

public class LogEntry {

    private String logID;
    private String timestamp;

    public LogEntry(){

    }

    public LogEntry(String logID, String timestamp){
        this.logID = logID;
        this.timestamp = timestamp;
    }

    public String getLogID(){
        return logID;
    }

    public String getTimestamp(){
        return timestamp;
    }
}
