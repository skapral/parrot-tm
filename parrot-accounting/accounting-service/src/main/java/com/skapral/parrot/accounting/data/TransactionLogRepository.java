package com.skapral.parrot.accounting.data;

import org.springframework.data.repository.CrudRepository;

public interface TransactionLogRepository extends CrudRepository<TransactionLog, Integer> {
}
