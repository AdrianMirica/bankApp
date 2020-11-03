package com.interview.bankApp.repository;

import com.interview.bankApp.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

    /**
     * This method queries the databes to find accounts by their accountNumber
     * @param accountNumber
     * @return {@code Account}
     */
    public Account findByAccountNumber(String accountNumber);

}
