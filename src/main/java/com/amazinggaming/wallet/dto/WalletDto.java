package com.amazinggaming.wallet.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author dereshharry
 * 
 * Used for Wallet related methods
 * can be update if additional fields are needed
 *
 */
@JsonInclude(value = Include.NON_NULL)
public class WalletDto extends BaseDto{
	
	private String password;

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
