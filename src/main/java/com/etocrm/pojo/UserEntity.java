package com.etocrm.pojo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity implements  Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6825589821644237578L;

	private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 权限
     */
    private String roles;



}
