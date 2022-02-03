/**
 * 
 */
package com.amazinggaming.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amazinggaming.wallet.dto.WalletDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.service.WalletService;

/**
 * @author dereshharry
 * 
 * Controller for wallet Entity
 *
 */
@RestController
@RequestMapping("/v1/api/wallet")
public class WalletController {

	@Autowired
	private WalletService walletService;

	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public ResponseEntity<?> getBalances(@RequestParam(required = true) String username) throws ResponseStatusException {
		try {
			return ResponseEntity.ok(walletService.balance(username));
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
		
	}
	
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public ResponseEntity<?> withdraw(@RequestBody WalletDto walletDto) throws ResponseStatusException {
		try {
			return ResponseEntity.ok(walletService.withdrawl(walletDto));
		}catch (ApplicationException e) {
			throw new ResponseStatusException(
			           HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public ResponseEntity<?> deposit(@RequestBody WalletDto walletDto) throws ResponseStatusException {
		try {
			return ResponseEntity.ok(walletService.deposit(walletDto));
		}catch (ApplicationException e) {
			throw new ResponseStatusException(
			           HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

}
