package com.dorsa.ModelsEmail;

import java.io.Serializable;

public class EmailSenderModel implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7809475889894783633L;
	private int propid;
	private String propname;
	private String propvalue;

	public EmailSenderModel() {
		// TODO Auto-generated constructor stub
	}

	public int getPropid() {
		return propid;
	}

	public void setPropid(int propid) {
		this.propid = propid;
	}

	public String getPropname() {
		return propname;
	}

	public void setPropname(String propname) {
		this.propname = propname;
	}

	public String getPropvalue() {
		return propvalue;
	}

	public void setPropvalue(String propvalue) {
		this.propvalue = propvalue;
	}

}
