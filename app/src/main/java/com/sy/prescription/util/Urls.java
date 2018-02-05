package com.sy.prescription.util;

public class Urls {

    /******
     * 测试接口地址
     ********/
    public final static String API_URL = "http://hd.shop.sh.cn/";
    public final static String API_VERSION_1 = "v1/";

    // 接口方法类型
    public final static class MethodType {
        public final static int GET = 0;
        public final static int POST = 1;
    }

    // 接口方法列表
    public final static class Method {
        public final static String SIGN = "index.php?ctl=biz_user&act=dologin";
        public final static String SIGN_OUT = "/api/logout";
        public final static String INIT = "/api/init";
        public final static String PAY = "/api/cheques";
        public final static String GATHER = "index.php?ctl=biz_api&act=gather";
        public final static String TREND = "/api/trend";
        public final static String CASH_PAY = "";
        public final static String SEARCH = "index.php?ctl=biz_api&act=search";
        public final static String DETAIL = "/api/detail";
        public final static String REFUND = "/api/refund";
        public final static String UPDATE = "api.php?act=version&";
        public final static String PRINT = "/api/print";
        public final static String OUT_TICKET = "index.php?ctl=biz_dealv&act=sale_coupon";
        public final static String CHECK_COUPON = "ticket.php?m=verify";
        public final static String STORAGE = "api.php?m=storage";
    }
}
