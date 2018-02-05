package com.sy.prescription.model;

public class Template<T> extends Basic {

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ClubCategory [data=" + data + "]";
	}

}
