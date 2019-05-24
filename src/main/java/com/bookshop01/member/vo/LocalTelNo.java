package com.bookshop01.member.vo;

import org.springframework.stereotype.Component;

@Component("localTelNo")
public class LocalTelNo {
 private int seq;
 private String localNo;
 
public LocalTelNo() {}

public LocalTelNo(int seq, String localNo) {
	this.seq = seq;
	this.localNo = localNo;
}

public int getSeq() {
	return seq;
}

public void setSeq(int seq) {
	this.seq = seq;
}

public String getLocalNo() {
	return localNo;
}

public void setLocalNo(String localNo) {
	this.localNo = localNo;
}
 


 
}
