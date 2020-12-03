package com.romang.homeexercise.unbound.model;

public class CRPGSignDTO {
	private String data;
	private String signature;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "CRPGSignDTO [data=" + data + ", signature=" + signature + "]";
	}

	
}
