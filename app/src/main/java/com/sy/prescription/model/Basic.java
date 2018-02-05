package com.sy.prescription.model;

public class Basic {
    /**
     * status = 1 成功  0 失败
     * msg = (  status = 0的时候有内容 返回的是错误详情，你可以直接提示，等1的时候返回是空"")
     * data (  status = 0的时候为空  status = 1的时候是array数据 所有取数据都从data取 其他的两个只是通讯消息
     */
    //data= 加密的字符串
    private int status;
    private String msg;
    private String sess_id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSessId() {
        return sess_id;
    }

    public void setSessId(String sess_id) {
        this.sess_id = sess_id;
    }
}
