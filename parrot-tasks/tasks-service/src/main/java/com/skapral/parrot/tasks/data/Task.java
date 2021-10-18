package com.skapral.parrot.tasks.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id private Integer id;
    private String description;
    private Status status;
    private Integer assignee;
}
