package com.ucash_test.lirongyunindialoan.myosotisutils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;

/**
 * IO utils
 *
 * @author prayer
 */

public class IOUtils {
    
    private IOUtils() {
        throw new AssertionError();
    }
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
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException! ", e);
            }
        }
    }
    
}
