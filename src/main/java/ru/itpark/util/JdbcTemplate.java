package ru.itpark.util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class JdbcTemplate {
    public JdbcTemplate() {
    }

    public static <T> T execute(String url, String sql, PreparedStatementExecutor<T> executor) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            return executor.execute(statement);
        }
    }

    public static <T> List<T> executeQuery(String url, String sql, util.PreparedStatementSetter setter, RowMapper<T> mapper) throws SQLException {
        return execute(url, sql, stmt -> {
            setter.set(stmt);
            try (ResultSet resultSet = stmt.executeQuery();) {
                List<T> result = new LinkedList<>();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
                return result;
            }
        });
    }


}

