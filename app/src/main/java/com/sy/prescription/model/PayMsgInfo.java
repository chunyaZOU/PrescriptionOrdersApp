package com.sy.prescription.model;

/**
 * Created by zcy on 2017/3/16.
 */

public class PayMsgInfo {
    /*{
        "type":"OrderNotify",
            "data":{

                "merchant_name":"",
                "out_trade_no":"20170316113019167",
                "fee_type":"CNY",
                "mch_id":103885063,
                "total_fee":1,
                "trade_type":"MICROPAY",
                "transaction_id":"4009232001201703163533881421",
                "body":"萨莉亚-微信支付",
                "status":4,
                "create_time":"2017-03-16 11:30:20",
                "pay_time":"2017-03-16 11:30:25",
                "user_name":"admin"
        }
    }*/

    private String type;
    private Data data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String out_trade_no;
        private String fee_type;
        private String mch_id;
        private String total_fee;
        private String trade_type;
        private String transaction_id;
        private String body;
        private int status;
        private String create_time;
        private String pay_time;
        private String user_name;
        private String op_user_id;
        private String merchant_name;

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }


        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;

        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getOp_user_id() {
            return op_user_id;
        }

        public void setOp_user_id(String op_user_id) {
            this.op_user_id = op_user_id;
        }
    }
}