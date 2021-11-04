package com.skapral.parrot.itests.assertions.jdbc;

import com.pragmaticobjects.oo.tests.Assertion;

import javax.sql.DataSource;

public class AssertAssumingDbState implements Assertion {
    private final DataSource datasource;
    private final String sql;
    private final Assertion assertion;

    public AssertAssumingDbState(DataSource datasource, String sql, Assertion assertion) {
        this.datasource = datasource;
        this.sql = sql;
        this.assertion = assertion;
    }

    @Override
    public final void check() throws Exception {
        try(var connection = datasource.getConnection()) {
            try(var statement = connection.createStatement()) {
                statement.execute(sql);
            }
        }
        assertion.check();
    }
}
