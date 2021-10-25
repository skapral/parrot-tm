package com.skapral.parrot.auth.data;

import com.skapral.parrot.common.data.SpringDataJdbc;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories
@Import(SpringDataJdbc.class)
public class Repositories {
}
