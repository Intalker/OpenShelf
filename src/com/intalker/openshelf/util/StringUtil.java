package com.intalker.openshelf.util;

public class StringUtil {

	public static boolean isEmpty(String str) {
		if (null == str) {
			return true;
		}
		if (str.length() == 0) {
			return true;
		}
		if (str.toLowerCase().compareTo("null") == 0) {
			return true;
		}
		return false;
	}
}
