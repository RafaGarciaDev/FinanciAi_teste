package dao;

import model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conexao;

    public ClienteDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaClientes(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela clientes se não existir
    private void criarTabelaClientes() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INT PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "renda_mensal DOUBLE NOT NULL)";
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabela 'clientes' verificada/criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'clientes': " + e.getMessage(), e);
        }
    }

    // Método para verificar se um cliente já existe
    private boolean clienteExiste(int id) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se cliente existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para adicionar um cliente
    public void adicionarCliente(Cliente cliente) {
        if (clienteExiste(cliente.getId())) {
            System.out.println("Cliente com ID " + cliente.getId() + " já existe no banco de dados.");
            return;
        }

        String sql = "INSERT INTO clientes (id, nome, renda_mensal) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNome());
            stmt.setDouble(3, cliente.getRendaMensal());
            stmt.executeUpdate();
            System.out.println("Cliente adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar cliente: " + e.getMessage(), e);
        }
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("renda_mensal")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    // Método para fechar a conexão
    public void fecharConexao() {
        try {
            if (conexao != null) {
                conexao.close();
                //System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão: " + e.getMessage(), e);
        }
    }
}