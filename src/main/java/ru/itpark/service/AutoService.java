package ru.itpark.service;

import ru.itpark.domain.Auto;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoService {
    private final DataSource ds;

    public AutoService() throws NamingException, SQLException {
        Context context = new InitialContext();
        this.ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");

        try (
                final Connection connection = ds.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            statement.execute("CREATE TABLE IF NOT EXISTS autos (id TEXT PRIMARY KEY , name TEXT NOT NULL, description TEXT NOT NULL, imageUrl TEXT);");
        }
    }

    public List<Auto> getAll() throws SQLException {
        try (
                final Connection connection = ds.getConnection();
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery("SELECT id, name, description, imageUrl FROM autos")
        ) {
            final List<Auto> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Auto(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("imageUrl")
                ));
            }
            return list;
        }
    }

    public void create(String name, String description, String imageUrl) throws SQLException {
        try (
                final Connection connection = ds.getConnection();
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO autos (id, name, description, imageUrl) VALUES (?, ?, ?, ?)")
        ) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setString(4, imageUrl);
            statement.execute();
        }
    }

}
