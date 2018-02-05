package com.sy.prescription.model;

/**
 * Created by ygs on 2017/3/22.
 */

public class OutTicket extends Basic {
/*
{
    "status": 1,
    "msg": "",
    "sess_id": "obvanefmpn44vkbm7l6kfmcnb6",
    "data": {
        "page_title": "消费券验证",
        "biz_user_status": 1,
        "password": "38376330",
        "end_time": "2017年05月29日23:59:59前有效"
    }
}
*/

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data{

        private String title;
        private String biz_user_status;
        private String password;
        private String end_time;
        private String price;


        public String getBiz_user_status() {
            return biz_user_status;
        }

        public void setBiz_user_status(String biz_user_status) {
            this.biz_user_status = biz_user_status;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
