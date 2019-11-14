package ru.itpark.service;

//import netscape.security.UserTarget;
import ru.itpark.domain.Auto;
import ru.itpark.util.JdbcTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;

public class AutoService {
    private final DataSource ds;

    public AutoService() throws NamingException {
        Context context = new InitialContext();
        this.ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        String sql = "CREATE TABLE IF NOT EXISTS autos (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT);";
        JdbcTemplate.executeUpdate(ds, sql, statement -> statement);
    }

    public List<Auto> getAll() {
        final String sql = "SELECT id, name, description, imageUrl FROM autos";
        return JdbcTemplate.executeQuery(ds, sql,
                resultSet -> new Auto(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("imageUrl")
                )
        );
    }

    public Auto create(String name, String description, String imageUrl) {
        String sql = "INSERT INTO autos (name, description, imageUrl) VALUES (?, ?, ?)";
        int autoId;
        autoId = JdbcTemplate.executeUpdate(ds, sql, statement -> {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, imageUrl);
            return statement;
        });
        return new Auto(autoId, name, description, imageUrl);
    }

}
