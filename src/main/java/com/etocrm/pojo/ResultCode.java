package com.etocrm.pojo;



public enum ResultCode {
	/**
	 * 成功
	 */
	SUCCESS(200),
	/**
	 * 失败
	 */
    FAIL(400),
    
    /**
     * 未认证（签名错误）
     */
    UNAUTHORIZED(401),
    
    /**
     * 接口不存在
     */
    NOT_FOUND(404),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500),
    
    /**
     *  业务异常
     */
    SERVICE_FAIL(402),
	
	/**
     *  支付密码错误
     */
    PAYPWD_ERROR(405),
	
	/**
     *  用户密码错误
     */
	PWD_ERROR(406);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
