package com.sy.prescription.model;

import java.util.List;

/**
 * Created by zcy on 2017/3/20.
 */

public class SearchResultInfo extends Basic {


        /*{"status":1,"msg":"查询成功","data":{"total":43,"page":20,"current_page":1,"rows":[
        {"out_trade_no":"20170322053548472", "time_start":"1490175348","total_fee":"0.0100","pay_type":"未知状态","status":"已退款"},
        {"out_trade_no":"20170321100735159","time_start":"1490105255","total_fee":"5.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321100643381","time_start":"1490105203","total_fee":"5.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321095005282","time_start":"1490104206","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321093805482","time_start":"1490103486","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321093207341","time_start":"1490103127","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321093204519","time_start":"1490103124","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321093139185","time_start":"1490103099","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321054112700","time_start":"1490089272","total_fee":"5.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321012742335","time_start":"1490074062","total_fee":"2.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321012552440","time_start":"1490073952","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321011518715","time_start":"1490073319","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321011506183","time_start":"1490073306","total_fee":"8.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321011452876","time_start":"1490073292","total_fee":"9.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321010815530","time_start":"1490072895","total_fee":"5.0000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321101231611","time_start":"1490062351","total_fee":"0.0100","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321100353885","time_start":"1490061833","total_fee":"0.2000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321100340376","time_start":"1490061820","total_fee":"0.2000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321100337254","time_start":"1490061817","total_fee":"0.2000","pay_type":"未知状态","status":"未支付"},
        {"out_trade_no":"20170321100337813", "time_start":"1490061817","total_fee":"0.2000","pay_type":"未知状态","status":"未支付"}]}}*/

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int total;
        private int page;
        private int current_page;
        private String total_count;
        private String total_fee;
        private List<ItemInfo> rows;


        public String getTotalCount() {
            return total_count;
        }

        public String getTotalFee() {
            return total_fee;
        }

        public String setTotalCount(String total_count) {
            return this.total_count = total_count;
        }

        public String setTotalFee(String total_fee) {
            return this.total_fee = total_fee;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public List<ItemInfo> getRows() {
            return rows;
        }

        public void setRows(List<ItemInfo> rows) {
            this.rows = rows;
        }

        public class ItemInfo {
            private String time_start;
            private String out_trade_no;
            private String pay_type;
            private String total_fee;
            private String status;

            public String getTime_start() {
                return time_start;
            }

            public void setTime_start(String time_start) {
                this.time_start = time_start;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getPay_type() {
                return pay_type;
            }

            public void setPay_type(String pay_type) {
                this.pay_type = pay_type;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
