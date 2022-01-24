package com.ngx.loans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ngx.loans.config.LoansServiceConfig;
import com.ngx.loans.model.Customer;
import com.ngx.loans.model.Loans;
import com.ngx.loans.model.Properties;
import com.ngx.loans.repository.LoansRepository;

@RestController
public class LoansController {

	@Autowired
	private LoansRepository loansRepository;

	@Autowired
	LoansServiceConfig loansConfig;
	
	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestBody Customer customer){
		System.out.println("Invoking Loans Microservice");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		if(loans!=null) {
			return loans;
		}else {
			return null;
		}
	}
	
	@GetMapping("/loans/properties")
	public String getProperties() throws JsonProcessingException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
}
