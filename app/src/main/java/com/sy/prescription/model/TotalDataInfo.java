package com.sy.prescription.model;

import java.util.List;

/**
 * Created by zcy on 2017/3/17.
 */

public class TotalDataInfo extends Basic {

    /*{"status":1,"msg":"查询成功","data":{"orderSum":{"count":2,"sum":"1.0100"},
    "refundSum":{"count":1,"sum":"0.0100"},"list":{"total":2,"page":20,"current_page":1,
            "rows":[{"out_trade_no":"20170316090916184","time_start":"2017-03-16 21:09:16","total_fee":"1.0000",
            "pay_type":"","status":"支付成功"},
        {"out_trade_no":"20170314025217673","time_start":"2017-03-14 14:52:17","total_fee":"0.0100",
        "pay_type":"","status":"已退款"}]}}}*/

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private CommenSum orderSum;
        private CommenSum refundSum;

        public CommenSum getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(CommenSum orderSum) {
            this.orderSum = orderSum;
        }

        public CommenSum getRefundSum() {
            return refundSum;
        }

        public void setRefundSum(CommenSum refundSum) {
            this.refundSum = refundSum;
        }

        public class CommenSum {
            private int count;
            private String sum;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }
        }

        private ItemList list;

        public ItemList getList() {
            return list;
        }

        public void setList(ItemList list) {
            this.list = list;
        }

        public class ItemList {
            private int total;
            private int page;
            private int current_page;
            private List<ItemInfo> rows;

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
                private String out_trade_no;
                private String time_start;
                private String total_fee;
                private String pay_type;
                private String status;

                public String getOut_trade_no() {
                    return out_trade_no;
                }

                public void setOut_trade_no(String out_trade_no) {
                    this.out_trade_no = out_trade_no;
                }

                public String getTime_start() {
                    return time_start;
                }

                public void setTime_start(String time_start) {
                    this.time_start = time_start;
                }

                public String getTotal_fee() {
                    return total_fee;
                }

                public void setTotal_fee(String total_fee) {
                    this.total_fee = total_fee;
                }

                public String getPay_type() {
                    return pay_type;
                }

                public void setPay_type(String pay_type) {
                    this.pay_type = pay_type;
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
}
