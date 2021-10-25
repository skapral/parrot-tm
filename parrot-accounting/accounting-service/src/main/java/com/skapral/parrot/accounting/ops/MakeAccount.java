package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.accounting.data.Account;
import com.skapral.parrot.accounting.data.AccountsRepository;

public class MakeAccount implements Operation {
    private final AccountsRepository repository;
    private final Integer accountId;
    private final String accountLogin;

    public MakeAccount(AccountsRepository repository, Integer accountId, String accountLogin) {
        this.repository = repository;
        this.accountId = accountId;
        this.accountLogin = accountLogin;
    }

    @Override
    public final void execute() {
        var account = new Account(accountId, accountLogin);
        if(repository.existsById(accountId)) {
            throw new RuntimeException("Account id conflict - " + accountId);
        }
        repository.save(account);
    }
}
