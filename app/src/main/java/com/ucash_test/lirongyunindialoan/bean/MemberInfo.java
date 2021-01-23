package com.ucash_test.lirongyunindialoan.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MemberInfo
{
    private Info login;

    public Info getInfo() {
        return login;
    }

    public class Info {
        private String id;//289,
        private int userId;//73993,
        private String name;//null,
        private String idNo;//null,
        private String balance;//"0.00",
        private int credit_approved;//0,

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
        private int edAmount;//0,
        private int bankCard;//0,
        private int isFace;//1,
        private int isStuff;//0,
        private int isSms;//1,
        private int isContacts;//0,
        private int isAddress;//1,
        private int miguanAuth;//0,
        private int mifengAuth;//0,
        private int chsiAuth;//0,
        private int payPassword;//0,
        private int infoAuth;//1,
        private long createTime;//1575267711000,
        private String authStatus;//"noauthorize",
        private String scAuthStatus;//"noauthorize",
        private String shareCode;//"QQYP15874925407",
        private int isCarrier;//0,
        private int isQjld;//0,
        private int isTz;//0,
        private int isTaobaopay;//0,
        private String picImg;//null,
        private String nickname;//null,
        private int isContactInfo;//0,
        private int isCompanyInfo;//0,
        private int integral;//20,
        private int isAppName;//1
        private int lbuma;
        private int personal;
        private int productStatus;//7天产品：1 14天产品：2

        public String getId() {
            return id;
        }

        public int getIsFace() {
            return isFace;
        }

        public int getIsSms() {
            return isSms;
        }

        public int getIsAddress() {
            return isAddress;
        }

        public int getIsAppName() {
            return isAppName;
        }

        public int getLbuma() {
            return lbuma;
        }

        public int getPersonal() {
            return personal;
        }

        public int getBankCard() {
            return bankCard;
        }

        public String getAuthStatus() {
            return authStatus;
        }

        public String getName() {
            return name;
        }

        public int getProductStatus() {
            return productStatus;
        }

        public int getCredit_approved() {
            return credit_approved;
        }

        public void setAuthStatus(String authStatus) {
            this.authStatus = authStatus;
        }
    }
}