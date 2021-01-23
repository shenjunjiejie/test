package com.lirongyuns.happyrupees.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 获取手机安装程序工具类
 */
public class AppInfoUtils {

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
    /**
     * 获取已经安装程序
     * @param context context
     * @return ArrayList<AppInfo>
     */
    public static ArrayList<AppInfo> getInstallPackages(Context context) {
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        PackageManager pkgManager = context.getPackageManager();
        List<PackageInfo> pkgs = pkgManager.getInstalledPackages(0);
        ArrayList<AppInfo> datas = new ArrayList<>();
        for (int i = 0; i < pkgs.size(); i++) {
            PackageInfo packageInfo = pkgs.get(i);
            // AppInfo 自定义类，包含应用信息
            AppInfo appInfo = new AppInfo();
            String app_name = packageInfo.applicationInfo.loadLabel(pkgManager).toString();//获取应用名称
            String verName = packageInfo.versionName;//获取应用版本名
            int verCode = packageInfo.versionCode;//获取应用版本号
            long firstInstallTime = packageInfo.firstInstallTime;
            long lastUpdateTime = packageInfo.lastUpdateTime;
            String packageName = packageInfo.packageName;
            appInfo.setAppName(app_name);
            appInfo.setVersionName(verName);
            appInfo.setVersionCode(verCode);
            appInfo.setPackageName(packageName);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
            Date firstDate = new Date(firstInstallTime);
            String firstDateFormat = simpleDateFormat.format(firstDate);
            Date lastDate = new Date(lastUpdateTime);
            String lastDateFormat = simpleDateFormat.format(lastDate);
            appInfo.setAppInstallTime(firstDateFormat);
            appInfo.setLastUpdateTime(lastDateFormat);
            System.out.println(appInfo.toString());
            datas.add(appInfo);
        }
        
        return datas;
    }
    
    
    /**
     * 应用实体类
     */
    public static  class AppInfo {
        private String appName;
        private String verName;
        private int verCode;
        private String appInstallTime;
        private String lastUpdateTime;
        private String packageName;
        
        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
        
        public String getAppName() {
            return appName;
        }
        
        public void setAppName(String app_name) {
            this.appName = app_name;
        }
        
        public String getVersionName() {
            return verName;
        }
        
        public void setVersionName(String versionName) {
            this.verName = versionName;
        }
        
        public int getVersionCode() {
            return verCode;
        }
        
        public void setVersionCode(int versionCode) {
            this.verCode = versionCode;
        }
        
        public String getAppInstallTime() {
            return appInstallTime;
        }
        
        public void setAppInstallTime(String app_install_time) {
            this.appInstallTime = app_install_time;
        }
        
        public String getLastUpdateTime() {
            return lastUpdateTime;
        }
        
        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
