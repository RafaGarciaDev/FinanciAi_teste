package dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // URL ajustada com o nome do banco, porta e sslMode=REQUIRED
    private static final String URL = "jdbc:mysql://finaciaitext-rafaelgarcia86-f678.g.aivencloud.com:21130/defaultdb?sslMode=REQUIRED";
    private static final String USUARIO = "avnadmin";
    private static final String SENHA = "AVNS_UehnzHsZwgGsPNb0Cuq";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}