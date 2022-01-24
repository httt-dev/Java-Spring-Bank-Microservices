package com.ngx.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ngx.accounts.config.AccountsServiceConfig;
import com.ngx.accounts.model.Accounts;
import com.ngx.accounts.model.Cards;
import com.ngx.accounts.model.Customer;
import com.ngx.accounts.model.CustomerDetails;
import com.ngx.accounts.model.Loans;
import com.ngx.accounts.model.Properties;
import com.ngx.accounts.repository.AccountsRepository;
import com.ngx.accounts.service.client.CardsFeignClient;
import com.ngx.accounts.service.client.LoansFeignClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class AccountsController {
	
	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	AccountsServiceConfig accountsConfig;   
	
	@Autowired
	LoansFeignClient loansFeignClient;
	
	@Autowired
	CardsFeignClient cardsFeignClient;
	
	@PostMapping("/myAccount")
	public Accounts getAccountDetails(@RequestBody Customer customer) {

		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if (accounts != null) {
			return accounts;
		} else {
			return null;
		}

	}

	/**
	 * Lay thong tin trong file cau hinh tren server
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties  properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(),accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
	
	@PostMapping("/myCustomerDetails")
//	@CircuitBreaker(name="detailsForCustomerSupportApp",fallbackMethod ="myCustomerDetailsFallBack" )
	@Retry(name = "retryForCustomerDetails" , fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		return customerDetails;
	}
	
	private CustomerDetails myCustomerDetailsFallBack(Customer customer , Throwable throwable) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		return customerDetails;
	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name="sayHello" , fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, rate limit test";
	}
	
	private String sayHelloFallback(Throwable t) {
		return "Hello, rate limit fallback test";

	}
}
