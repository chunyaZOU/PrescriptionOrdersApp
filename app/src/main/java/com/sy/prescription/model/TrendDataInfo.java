package com.sy.prescription.model;

/**
 * Created by zcy on 2017/3/17.
 */

public class TrendDataInfo extends Basic {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private Sale sale;

        public Sale getSale() {
            return sale;
        }

        public void setSale(Sale sale) {
            this.sale = sale;
        }

        public class Sale {
            private String[] time;
            private float[] money;
            private double total;
            private String data;

            public String[] getTime() {
                return time;
            }

            public void setTime(String[] time) {
                this.time = time;
            }

            public float[] getMoney() {
                return money;
            }

            public void setMoney(float[] money) {
                this.money = money;
            }

            public double getTotal() {
                return total;
            }

            public void setTotal(double total) {
                this.total = total;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }
    }
}