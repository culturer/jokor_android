package com.jokor.base.wedgit.util;

public class QrCodeUtil {
	
	public static class QRInfo{
		public static final int QR_TYPE_TEL = 0;
		public static final int QR_TYPE_WIFI = 1;
		public static final int QR_TYPE_WEB = 2;
		public static final int QR_TYPE_DEVICE = 3;
		public static final int QR_TYPE_CIRCLE = 4;

		public int type = 0;
		public String msg = "";
		
		public QRInfo() {
		}
		
		public QRInfo(int type, String msg) {
			this.type = type;
			this.msg = msg;
		}
	}
}
