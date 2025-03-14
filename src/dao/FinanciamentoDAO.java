package dao;

import model.entities.Financiamento;
import model.enums.TipoAmortizacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanciamentoDAO {
    private Connection conexao;

    public FinanciamentoDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaFinanciamentos(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela financiamentos se não existir
    private void criarTabelaFinanciamentos() {
        String sql = "CREATE TABLE IF NOT EXISTS financiamentos (" +
                "id INT PRIMARY KEY, " +
                "cliente_id INT NOT NULL, " +
                "imovel_id INT NOT NULL, " +
                "valor_financiado DOUBLE NOT NULL, " +
                "taxa_juros DOUBLE NOT NULL, " +
                "valor_entrada DOUBLE NOT NULL, " +
                "prazo INT NOT NULL, " +
                "tipo_amortizacao VARCHAR(50) NOT NULL, " +
                "total_pagar DOUBLE NOT NULL, " +
                "data_simulacao DATE NOT NULL)";
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabela 'financiamentos' verificada/criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'financiamentos': " + e.getMessage(), e);
        }
    }

    // Método para verificar se um financiamento já existe
    private boolean financiamentoExiste(int id) {
        String sql = "SELECT COUNT(*) FROM financiamentos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se financiamento existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para adicionar um financiamento
    public void adicionarFinanciamento(Financiamento financiamento) {
        if (financiamentoExiste(financiamento.getId())) {
            System.out.println("Financiamento com ID " + financiamento.getId() + " já existe no banco de dados.");
            return;
        }

        String sql = "INSERT INTO financiamentos (id, cliente_id, imovel_id, valor_financiado, taxa_juros, valor_entrada, prazo, tipo_amortizacao, total_pagar, data_simulacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, financiamento.getId());
            stmt.setInt(2, financiamento.getClienteId());
            stmt.setInt(3, financiamento.getImovelId());
            stmt.setDouble(4, financiamento.getValorFinanciado());
            stmt.setDouble(5, financiamento.getTaxaJuros());
            stmt.setDouble(6, financiamento.getValorEntrada());
            stmt.setInt(7, financiamento.getPrazo());
            stmt.setString(8, financiamento.getTipoAmortizacao().toString());
            stmt.setDouble(9, financiamento.getTotalPagar());
            stmt.setDate(10, Date.valueOf(financiamento.getDataSimulacao()));
            stmt.executeUpdate();
            System.out.println("Financiamento adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar financiamento: " + e.getMessage(), e);
        }
    }

    // Método para listar todos os financiamentos
    public List<Financiamento> listarFinanciamentos() {
        List<Financiamento> financiamentos = new ArrayList<>();
        String sql = "SELECT * FROM financiamentos";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Financiamento financiamento = new Financiamento(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("imovel_id"),
                        rs.getDouble("valor_financiado"),
                        rs.getDouble("taxa_juros"),
                        rs.getDouble("valor_entrada"),
                        rs.getInt("prazo"),
                        TipoAmortizacao.valueOf(rs.getString("tipo_amortizacao")),
                        rs.getDouble("total_pagar"),
                        rs.getDate("data_simulacao").toLocalDate()
                );
                financiamentos.add(financiamento);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar financiamentos: " + e.getMessage(), e);
        }
        return financiamentos;
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