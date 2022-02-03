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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amazinggaming.wallet.dto.BetDto;
import com.amazinggaming.wallet.exception.ApplicationException;
import com.amazinggaming.wallet.service.BetService;

/**
 * @author dereshharry
 * 
 * rest controller for all bet entity
 *
 */
@RestController
@RequestMapping("/v1/api/bet")
public class BetController {
	
	@Autowired
	private BetService betService;
	
	@RequestMapping(value = "/place", method = RequestMethod.POST)
	public ResponseEntity<?> placeBet(@RequestBody BetDto betDto) throws ResponseStatusException {
		try {
			return ResponseEntity.ok(betService.bet(betDto));
		}catch (ApplicationException e) {
			throw new ResponseStatusException(
			           HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = "/win", method = RequestMethod.POST)
	public ResponseEntity<?> winBet(@RequestBody BetDto betDto) throws ResponseStatusException {
		try {
			return ResponseEntity.ok(betService.win(betDto));
		}catch (ApplicationException e) {
			throw new ResponseStatusException(
			           HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
	
	
	

}
