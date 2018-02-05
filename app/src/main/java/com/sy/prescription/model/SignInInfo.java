package com.sy.prescription.model;

/**
 * Created by zcy on 2017/3/15.
 */

public class SignInInfo extends Basic {


    //{"status":1,"msg":"登陆成功!","data":{"auto":"haVeTVmc-7iAkteTHOSQ3PNLs8VGfPdSGNGLcGSM3qNSVKr-cEH6_","identifier":"7","user_sign":"eJxlz0FPgzAUB-A7n4JwnTEFWoElHmDbQYWJ28x0l6aj7dItY*VRiNP43VVcYhPPv-977-8*HNd1vVW*vGZVdepqQ81ZC88dux7yrv5Qa8UpMzQE-g-Fm1YgKJNGwIABSQKE7IjiojZKqksgsqjlBzqsH8TH34MoRCSwI2o3YDF7ntylu61M9odyw3B-rmCjw*MeujyLO9zlU16X0-ZlgWST9UWqZilkMA-E4ytXeD3fjoqiiZrYYBY9yRYWMNFluR49vN8vDbq1Thp1FJdCcUJuoggRS3sBrTrVv88in-i*n-z09pxP5wsvulvk","token":"0jm21vr06s6jh49brscc460u01"}}
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int id ;
        private String account_name;
        private String account_password;
        private String sess_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
        }

        public String getAccount_password() {
            return account_password;
        }

        public void setAccount_password(String account_password) {
            this.account_password = account_password;
        }

        public String getSess_id() {
            return sess_id;
        }

        public void setSess_id(String sess_id) {
            this.sess_id = sess_id;
        }

    }
}