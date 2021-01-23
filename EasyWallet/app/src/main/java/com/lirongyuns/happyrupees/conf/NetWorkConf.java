package com.lirongyuns.happyrupees.conf;

public class NetWorkConf {
    private String HTTP_HOST = "https://server.magicrupees.com/";//线上
    private String HTTP_HOST2 = "http://n208188y70.zicp.vip:59511/";//定制版
    private String HTTP_HOST3 = "http://n208188y70.zicp.vip/";//标准版

    public NetWorkConf(){

    }

    private static class NetWorkHolder{
        private final static NetWorkConf NETWORK_CONF = new NetWorkConf();
    }

    public String getHttpHost(){
        return HTTP_HOST2;
    }
    public static NetWorkConf getInstance(){
        return new NetWorkHolder().NETWORK_CONF;
    }
}
