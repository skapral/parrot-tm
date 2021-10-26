package com.skapral.parrot.tasks.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id private UUID id;
    private String description;
    private Status status;
    private UUID assignee;
}
