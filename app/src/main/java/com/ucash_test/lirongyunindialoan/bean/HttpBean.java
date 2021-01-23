package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * JSON基础解析类
 */
public class HttpBean
{
    protected int code;//结果码, 成功 ：200, 失败：500
    
    protected String msg;//消息

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
