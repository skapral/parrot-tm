package com.skapral.parrot.itests.assertions.jdbc;

import com.pragmaticobjects.oo.tests.Assertion;
import org.assertj.db.type.Table;

import javax.sql.DataSource;

import static org.assertj.db.api.Assertions.assertThat;

public class AssertTableHasNumberOfRows implements Assertion {
    private final DataSource dataSource;
    private final String tableName;
    private final int expectedNumberOfRows;

    public AssertTableHasNumberOfRows(DataSource dataSource, String tableName, int expectedNumberOfRows) {
        this.dataSource = dataSource;
        this.tableName = tableName;
        this.expectedNumberOfRows = expectedNumberOfRows;
    }

    @Override
    public final void check() throws Exception {
        var table = new Table(dataSource, tableName);
        assertThat(table).exists();
        assertThat(table).hasNumberOfRows(expectedNumberOfRows);
    }
}
