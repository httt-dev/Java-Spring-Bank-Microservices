package com.ngx.loans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ngx.loans.model.Customer;
import com.ngx.loans.model.Loans;
import com.ngx.loans.repository.LoansRepository;

@RestController
public class LoansController {

	@Autowired
	private LoansRepository loansRepository;
	@PostMapping("/myLoans")
	public List<Loans> getLoansDetails(@RequestBody Customer customer){
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		if(loans!=null) {
			return loans;
		}else {
			return null;
		}
	}
}
