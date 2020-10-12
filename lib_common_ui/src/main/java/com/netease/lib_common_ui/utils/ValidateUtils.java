package com.netease.lib_common_ui.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {
	private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

	/**
	 * 判断是否是手机号
	 *
	 * @param tel 手机号
	 * @return boolean true:是  false:否
	 */
	public static boolean isMobile(String tel) {
		if (TextUtils.isEmpty(tel)) {
			return false;
		}
		return Pattern.matches(REGEX_MOBILE, tel);
	}

	/**
	 * 判断密码是否符合规范
	 *
	 * @param password 输入的密码
	 * @return boolean true:是  false:否
	 */
	public static boolean isPassword(String password) {
		String regex = "^[a-zA-Z0-9\\u4E00-\\u9FA5]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(password);
		return m.matches();
	}
}
