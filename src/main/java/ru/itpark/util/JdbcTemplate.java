package ru.itpark.util;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class JdbcTemplate {
    private JdbcTemplate() {
    }

    public static <T> List<T> executeQuery(DataSource ds, String sql, RowMapper<T> mapper) {
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            List<T> result = new LinkedList<>();

            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }

    public static void executeUpdate(DataSource ds, String sql, String... args) {
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            int i = 1;
            for (String arg : args) {
                if (arg == null) {
                    throw new IllegalArgumentException("Unexpected null argument in INSERT statement");
                }
                statement.setString(i++, arg);
            }
            statement.execute();
        } catch (SQLException e) {
            throw new SqlMappingException(e);
        }
    }


}
