package com.jokor.base.util.pay.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2018/11/3 0003.
 */

public class PayBean {
	
	/**
	 * timeout_express : 30m
	 * product_code : QUICK_MSECURITY_PAY
	 * total_amount : 0.01
	 * subject : 1
	 * body : 我是测试数据
	 * out_trade_no :
	 */
	
	private String timeout_express;
	private String product_code;
	private String total_amount;
	private String subject;
	private String body;
	private String out_trade_no;
	
	public PayBean(String total_amount, String body) {
		this.total_amount = total_amount;
		this.body = body;
		this.timeout_express = "30m";
		this.product_code = "QUICK_MSECURITY_PAY";
		this.subject = "支持jokor工作室~";
		this.out_trade_no = getOutTradeNo();
	}
	
	public String getTimeout_express() {
		return timeout_express;
	}
	
	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}
	
	public String getProduct_code() {
		return product_code;
	}
	
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
	public String getTotal_amount() {
		return total_amount;
	}
	
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getOut_trade_no() {
		return out_trade_no;
	}
	
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
	/**
	 * 要求外部订单号必须唯一。
	 * @return
	 */
	private static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
}
