/**
 * 
 */
package com.amazinggaming.wallet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amazinggaming.wallet.service.TransactionService;

/**
 * @author dereshharry
 *
 * Controller for Transaction Entity
 *
 */
@RestController
@RequestMapping("/v1/api/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllTransactions(@RequestParam(required = false) String description,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) throws ResponseStatusException{
		try {
			return new ResponseEntity<>(transactionService.getAllTransactions(description, page, size), HttpStatus.OK);
		}catch (Exception e) {
			throw new ResponseStatusException(
			           HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
		
	}

}
