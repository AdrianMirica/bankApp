package com.interview.bankApp.repository;

import com.interview.bankApp.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
}
