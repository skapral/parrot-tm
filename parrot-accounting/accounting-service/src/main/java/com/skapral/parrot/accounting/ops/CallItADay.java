package com.skapral.parrot.accounting.ops;

import com.skapral.parrot.accounting.data.AccountsRepository;
import com.skapral.parrot.accounting.data.TransactionLog;
import com.skapral.parrot.accounting.data.TransactionLogRepository;
import io.vavr.collection.List;

public class CallItADay implements Operation {
    private final TransactionLogRepository transactionsRepo;
    private final AccountsRepository accountsRepo;

    public CallItADay(TransactionLogRepository transactionsRepo, AccountsRepository accountsRepo) {
        this.transactionsRepo = transactionsRepo;
        this.accountsRepo = accountsRepo;
    }

    @Override
    public final void execute() {
        var ts = List.ofAll(accountsRepo.findAll())
                .map(acc -> {
                    var tl = new TransactionLog();
                    tl.setAccountId(acc.getId());
                    tl.setDescription("Working day over, time for payback");
                    tl.setValue(0);
                    return tl;
                });
        transactionsRepo.saveAll(ts);
    }
}
