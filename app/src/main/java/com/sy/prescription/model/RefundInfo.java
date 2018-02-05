package com.sy.prescription.model;

/**
 * Created by ygs on 2017/3/22.
 */

public class RefundInfo extends Basic {
/*
    {
        "msg": "退款成功！",
            "status": 1,
            "data": {
        "body":"退款签购单",
                "mch_id": 103885069,
                "out_refund_no": "101530023222201703227103867785",
                "out_trade_no": "10388506820170322110918243",
                "refund_fee": "1",
                "refund_id": "101530023222201703226101193969",
                "transaction_id": "101530023222201703227103867785",
                "user_name": "shudian2",
                "merchant_name": "郑州大摩纸的时代书店2",
                "pay_type": "微信支付",
                "fee_type": "CNY",
                "create_time": "2017-03-22 14:13:51",
                "remark": "已退款成功，资金原路返回支付账户1-10分钟到账"
    }
    }*/

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data{

        private int mch_id;
        private String out_refund_no;
        private String out_trade_no;

        private String refund_fee;
        private String refund_id;
        private String transaction_id;
        private String user_name;
        private String merchant_name;
        private String pay_type;
        private String fee_type;
        private String create_time;
        private String remark;

        public int getMch_id() {
            return mch_id;
        }

        public void setMch_id(int mch_id) {
            this.mch_id = mch_id;
        }

        public String getOut_refund_no() {
            return out_refund_no;
        }

        public void setOut_refund_no(String out_refund_no) {
            this.out_refund_no = out_refund_no;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getRefund_fee() {
            return refund_fee;
        }

        public void setRefund_fee(String refund_fee) {
            this.refund_fee = refund_fee;
        }

        public String getRefund_id() {
            return refund_id;
        }

        public void setRefund_id(String refund_id) {
            this.refund_id = refund_id;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
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

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
