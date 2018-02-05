package com.sy.prescription.model;

/**
 * Created by ygs on 2017/3/22.
 */

public class CouponInfo extends Basic {
/*
{
    "status": 1,
    "msg": "验票成功",
    "sess_id": "obvanefmpn44vkbm7l6kfmcnb6",
    "data": {
        "biz_user_status": 1,
        "is_auth": 1,
        "type": "3",
        "end_time": "2017-05-14"
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

        private int type;
        private String password;
        private String end_time;

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
