package com.bookshop01.member.vo;

import org.springframework.stereotype.Component;

@Component("couponVO")
public class CouponVO {
	private String member_id;
	private int balance;
	private int  shopmoney;
	private int coupon;
	private double point;
	private int sangpumgwon;
	private int digitalmoney;
	
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getShopmoney() {
		return shopmoney;
	}
	public void setShopmoney(int shopmoney) {
		this.shopmoney = shopmoney;
	}
	public int getCoupon() {
		return coupon;
	}
	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}
	public double getPoint() {
		return point;
	}
	public void setPoint(double point) {
		this.point = point;
	}
	public int getSangpumgwon() {
		return sangpumgwon;
	}
	public void setSangpumgwon(int sangpumgwon) {
		this.sangpumgwon = sangpumgwon;
	}
	public int getDigitalmoney() {
		return digitalmoney;
	}
	public void setDigitalmoney(int digitalmoney) {
		this.digitalmoney = digitalmoney;
	}
	
	

}
