package com.ngx.loans.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ngx.loans.model.Loans;

public interface LoansRepository extends CrudRepository<Loans,Long>{

	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);
}
