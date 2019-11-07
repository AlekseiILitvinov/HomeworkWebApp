package ru.itpark.service;

import ru.itpark.domain.Auto;
import ru.itpark.util.JdbcTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

public class AutoService {
    private final DataSource ds;

    public AutoService() throws NamingException {
        Context context = new InitialContext();
        this.ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        String sql = "CREATE TABLE IF NOT EXISTS autos (id TEXT PRIMARY KEY , name TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT);";
        JdbcTemplate.executeUpdate(ds, sql);
    }

    public List<Auto> getAll() {
        final String sql = "SELECT id, name, description, imageUrl FROM autos";
        return JdbcTemplate.executeQuery(ds, sql,
                resultSet -> new Auto(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("imageUrl")
                )
        );
    }

    public void create(String name, String description, String imageUrl) {
        String sql = "INSERT INTO autos (id, name, description, imageUrl) VALUES (?, ?, ?, ?)";
        JdbcTemplate.executeUpdate(ds, sql, UUID.randomUUID().toString(), name, description, imageUrl);
    }

}
