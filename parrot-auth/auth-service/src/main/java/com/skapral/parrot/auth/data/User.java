package com.skapral.parrot.auth.data;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@EObjectHint(enabled = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String login;
    private Role role;
}
