package com.sy.prescription.util;

import java.io.UnsupportedEncodingException;
import android.util.Base64;

public class EncryptUtil {

	public static String easyEncrypt(String str) {

		byte[] temp = getBase64(str + System.currentTimeMillis());
		byte j = temp[0];
		temp[0] = temp[temp.length - 1];
		temp[temp.length - 1] = j;

		j = temp[1];
		temp[1] = temp[temp.length - 2];
		temp[temp.length - 2] = j;

		try {
			return new String(temp, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getBase64(String str) {
		byte[] b = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			b = Base64.encode(b, Base64.NO_WRAP);
		}
		return b;
	}

	/*
	 * public static String easyDecryption(String str) { byte[] temp = null; try
	 * { temp = str.getBytes("utf-8"); } catch (UnsupportedEncodingException e1)
	 * { // TODO Auto-generated catch block e1.printStackTrace(); } byte j =
	 * temp[0]; temp[0] = temp[temp.length - 1]; temp[temp.length - 1] = j;
	 * 
	 * j = temp[1]; temp[1] = temp[temp.length - 2]; temp[temp.length - 2] = j;
	 * 
	 * temp = Base64.decode(temp, Base64.NO_WRAP);
	 * 
	 * try { String s = new String(temp, "utf-8"); return s = s.substring(0,
	 * s.length() - 13); } catch (UnsupportedEncodingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return null; }
	 */

}
