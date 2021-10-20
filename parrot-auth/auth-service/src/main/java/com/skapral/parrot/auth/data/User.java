package com.skapral.parrot.auth.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id private Integer id;
    private String login;
    private Role role;
}
