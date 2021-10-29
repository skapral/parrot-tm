package com.skapral.parrot.auth.data;

import com.skapral.parrot.common.data.SpringDataJdbc;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringDataJdbc.class)
public class Jdbc {
}
