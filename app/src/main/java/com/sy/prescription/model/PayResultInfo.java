package com.sy.prescription.model;

/**
 * Created by zcy on 2017/3/16.
 */

public class PayResultInfo extends Basic {
    /*{
    "status": 1,
    "code_img_url": "https://pay.swiftpass.cn/pay/qrcode?uuid=https://qr.alipay.com/bax05075vuqpqz4msvgy408c",
    "code_url": "https://qr.alipay.com/bax05075vuqpqz4msvgy408c",
    "order_sn": "20170316023922304",
    "amount": 100,
    "code_status": ""
    }*/

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private String code_img_url;
        private String code_url;
        private String order_sn;
        private int amount;
        private String code_status;

        public String getCode_img_url() {
            return code_img_url;
        }

        public void setCode_img_url(String code_img_url) {
            this.code_img_url = code_img_url;
        }

        public String getCode_url() {
            return code_url;
        }

        public void setCode_url(String code_url) {
            this.code_url = code_url;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCode_status() {
            return code_status;
        }

        public void setCode_status(String code_status) {
            this.code_status = code_status;
        }
    }

}
