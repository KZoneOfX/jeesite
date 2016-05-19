package com.thinkgem.jeesite.common.utils;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.constant.ConstantException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xiaoke Zhang
 * Date: 5/14/2016
 * Time: 8:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResultMapper<T> {

	private String code = ConstantException.sucess_code;
	private String message = ConstantException.sucess_message;
	private T datas;

	public ResultMapper() {
		super();
	}

	public Map<String, Object> getResultMap() {
		Map<String, Object> map = Maps.newLinkedHashMap();
		map.put("code", code);
		map.put("message", message);
		if (datas != null) {
			map.put("datas", datas);
		}
		return map;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}

}
