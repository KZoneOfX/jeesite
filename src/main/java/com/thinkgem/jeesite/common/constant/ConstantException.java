package com.thinkgem.jeesite.common.constant;

/**
 * Created with IntelliJ IDEA.
 * User: Xiaoke Zhang
 * Date: 5/14/2016
 * Time: 8:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ConstantException {
	/**
	 * 1000 接口请求成功响应码
	 */
	String sucess_code = "1000";
	String sucess_message = "success";
	/**
	 * 1001 参数错误返回码
	 */
	String args_error_code = "1001";
	String args_error_message = "参数错误";
	/**
	 * 1002 未查询到数据返回码
	 */
	String no_data_code = "1002";
	String no_data_message = "未查询到数据";
	/**
	 * 1003 异常信秘返回信息状态码
	 */
	String exception_code = "1003";
	String exception_message = "网络错误";
	/**
	 * 1004 数据已经存在返回码
	 */
	String exists_code = "1004";
	String exists_code_message = "数据已经存在";
	/**
	 * 1005 未定义的错误信息
	 */
	String save_data_err_code ="1005";
	String save_data_err_message = "数据保存出错";
	/**
	 * 1006 验证码过期
	 */
	String authcode_timeout_code = "1006";
	String authcode_timeout_message = "验证码过期";
	/**
	 * 1007 验证码错误
	 */
	String authcode_error_code = "1007";
	String authcode_error_message = "验证码错误";
	/**
	 * 1008 验证码发送频繁
	 */
	String authcode_frequency_code = "1008";
	String authcode_frequency_message = "验证码发送频繁";
	/**
	 * 1009 客户端版本过低
	 */
	String client_version_is_low_code = "1009";
	/**
	 * 1010 token过期
	 */
	String client_token_timeout_code = "1010";
	String client_token_timeout_message = "请重新登陆!";
}
