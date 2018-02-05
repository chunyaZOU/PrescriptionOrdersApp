package com.sy.prescription.model;

/**
 * Created by ygs on 2017/3/11.
 */

public class FormList {
    private int type;
    private String formId;
    private String amount;
    private String time;
    private String state;

    public FormList(int type,String formId,String amount,String time,String state){
        this.type=type;
        this.formId=formId;
        this.amount=amount;
        this.time=time;
        this.state=state;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
