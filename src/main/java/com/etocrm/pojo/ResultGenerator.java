
package com.etocrm.pojo;


import java.io.Serializable;

public class ResultGenerator implements Serializable {

	private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
	private static final long serialVersionUID = 1695213183390143942L;

	/**
	 * 创建成功Result
	 * @return
	 */
	public static Result genSuccessResult() {
		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
	}
	/**
	 * 创建 含 data的Result
	 * @param data
	 * @return
	 */
	public static Result genSuccessResult(Object data) {
		return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
	}
	/**
	 * 创建 失败的Result ， message为传入参数
	 * @param message
	 * @return
	 */
	public static Result genFailResult(String message) {
		return new Result().setCode(ResultCode.FAIL).setMessage(message);
	}
	/**
	 * 自定义返回code ，message
	 * @param code
	 * @param message
	 * @return
	 */
	public static Result genFailResult(Integer code,String message) {
		return new Result().setCode(code).setMessage(message);
	}

	public static String getDefaultSuccessMessage() {
		return DEFAULT_SUCCESS_MESSAGE;
	}

	
	/**
	* 判断result返回的是否是成功主体
	* @author      kevin
	* @param      result
	* @return      
	* @exception   
	* @date        2019/7/13 21:51
	*/
	public static boolean isSuccess(Result result) {
		return null != result && ResultCode.SUCCESS.code == result.getCode();
	}
}
