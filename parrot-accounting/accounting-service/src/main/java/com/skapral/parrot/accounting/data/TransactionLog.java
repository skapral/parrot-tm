package com.skapral.parrot.accounting.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog {
    @Id private Integer id;
    private Integer accountId;
    private Timestamp timestamp;
    private String description;
    private Integer value;
}
