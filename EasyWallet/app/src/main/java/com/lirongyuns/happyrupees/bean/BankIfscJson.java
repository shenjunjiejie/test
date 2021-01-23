package com.lirongyuns.happyrupees.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BankIfscJson extends HttpBean {
    
    
    private ArrayList<String> data;

    public ArrayList<String> getData() {
        return data;
    }
    
    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
