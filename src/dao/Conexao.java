package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // URL ajustada com o nome do banco, porta e sslMode=REQUIRED
    private static final String URL = "DB_PASSWORD";
    private static final String USUARIO = "DB_PASSWORD";
    private static final String ADB_PASSWORD" = "ADB_PASSWORD";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, ADB_PASSWORD");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}