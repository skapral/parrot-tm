package com.skapral.parrot.accounting.rest;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@EObjectHint(enabled = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog {
    private UUID id;
    private Timestamp time;
    private UUID accountId;
    private String description;
    private Integer debit;
    private Integer credit;
}
