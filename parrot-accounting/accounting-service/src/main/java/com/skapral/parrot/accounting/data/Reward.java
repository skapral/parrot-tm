package com.skapral.parrot.accounting.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {
    @Id private Integer taskId;
    private Integer reward;
    private Integer penalty;
}
