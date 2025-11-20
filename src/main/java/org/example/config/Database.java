package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:livros.db";
        return DriverManager.getConnection(url);
    }

    public static void initialize() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS livros (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT NOT NULL," +
                    "autor TEXT NOT NULL," +
                    "anoPublicacao INTEGER NOT NULL);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro iniciando banco de dados: " + e.getMessage());
        }
    }
}
