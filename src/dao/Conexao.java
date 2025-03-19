package dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USUARIO = dotenv.get("DB_USER");
    private static final String SENHA = dotenv.get("DB_PASSWORD");

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}
