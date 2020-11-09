package com.group10.cse5236project;

import android.app.Application;

public class infoClass {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    private static final infoClass holder = new infoClass();
    public static infoClass getInstance() {return holder;}
}
