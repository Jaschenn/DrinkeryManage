package com.mwq.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

	public static boolean execute(String rule, String content) {
		Pattern pattern = Pattern.compile(rule);// 利用验证规则创建Pattern对象
		Matcher matcher = pattern.matcher(content);// 利用验证内容获得Matcher对象
		return matcher.matches();// 返回验证结果
	}

}
