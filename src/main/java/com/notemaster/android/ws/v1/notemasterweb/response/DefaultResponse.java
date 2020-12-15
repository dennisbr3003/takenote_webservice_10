package com.notemaster.android.ws.v1.notemasterweb.response;

public class DefaultResponse {
	
	private String responsetype = "takenote10";  
	private String status;
	private String entity;
	private String key;	
	private String remark;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResponsetype() {
		return responsetype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}

}
