package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 内容实体
 * @param <T>
 */
public class ContentBean<T> extends HttpBean
{
    private T srxdContent;

    public void defeatCoding(){
        //花指令
        BufferedReader br =null;
        try {
            br = new BufferedReader(new FileReader("fakeFile"));
            String line;
            while((line=br.readLine())!= null){
                String[] splited = line.split(" +");
                if(splited.length >= 0){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                br.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public T getObj() {
        return srxdContent;
    }
}
