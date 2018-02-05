package com.sy.prescription.model;

/**
 * Created by ygs on 2017/3/22.
 */

/*
   {
    "msg": "success",
    "data": {
        "region": "A",
        "number": "2",
        "top": "3"
    },
    "status": 1
}
        */

public class StorageInfo extends Basic {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private int type;
        private int top;
        private String region;
        private String number;
        private String sn;
        private String password;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getSn() {
            return sn;
        }
        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
