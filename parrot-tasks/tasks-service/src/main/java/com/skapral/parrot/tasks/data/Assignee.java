package com.skapral.parrot.tasks.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignee {
    @Id private Integer id;
    private String name;
}
