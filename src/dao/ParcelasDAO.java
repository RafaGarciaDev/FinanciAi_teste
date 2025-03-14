package dao;

import model.entities.Parcelas;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParcelasDAO {
    private Connection conexao;

    public ParcelasDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaParcelas(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela parcelas se não existir
    private void criarTabelaParcelas() {
        String sql = "CREATE TABLE IF NOT EXISTS parcelas (" +
                "id INT PRIMARY KEY, " +
                "financiamento_id INT NOT NULL, " +
                "numero_parcela INT NOT NULL, " +
                "valor_parcela DOUBLE NOT NULL, " +
                "valor_amortizacao DOUBLE NOT NULL)";
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabela 'parcelas' verificada/criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'parcelas': " + e.getMessage(), e);
        }
    }

    // Método para verificar se uma parcela já existe
    private boolean parcelaExiste(int id) {
        String sql = "SELECT COUNT(*) FROM parcelas WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se parcela existe: " + e.getMessage(), e);
        }
        return false;
    }

    private double formatarValor(double valor) {
        DecimalFormat df = new DecimalFormat("#.##"); // Formata com duas casas decimais
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US)); // Usa ponto como separador decimal
        return Double.parseDouble(df.format(valor));
    }

    // Método para adicionar uma nova parcela
    public void adicionarParcela(Parcelas parcela) {
        if (parcelaExiste(parcela.getId())) {
            System.out.println("Parcela com ID " + parcela.getId() + " já existe no banco de dados.");
            return;
        }

        // Formata os valores antes de salvar
        double valorParcelaFormatado = formatarValor(parcela.getValorParcela());
        double valorAmortizacaoFormatado = formatarValor(parcela.getValorAmortizacao());

        String sql = "INSERT INTO parcelas (id, financiamento_id, numero_parcela, valor_parcela, valor_amortizacao) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, parcela.getId());
            stmt.setInt(2, parcela.getFinanciamentoId());
            stmt.setInt(3, parcela.getNumeroParcela());
            stmt.setDouble(4, valorParcelaFormatado); // Valor formatado
            stmt.setDouble(5, valorAmortizacaoFormatado); // Valor formatado
            stmt.executeUpdate();
            System.out.println("Parcela adicionada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar parcela: " + e.getMessage(), e);
        }
    }

    private void removerParcelasIntermediarias(int financiamentoId) {
        // Consulta para selecionar as 5 primeiras e as 5 últimas parcelas
        String sql = "DELETE FROM parcelas WHERE financiamento_id = ? AND id NOT IN (" +
                "(SELECT id FROM (SELECT id FROM parcelas WHERE financiamento_id = ? ORDER BY numero_parcela LIMIT 5) AS primeiras) " +
                "UNION " +
                "(SELECT id FROM (SELECT id FROM parcelas WHERE financiamento_id = ? ORDER BY numero_parcela DESC LIMIT 5) AS ultimas))";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, financiamentoId);
            stmt.setInt(2, financiamentoId);
            stmt.setInt(3, financiamentoId);
            stmt.executeUpdate();
            System.out.println("Parcelas intermediárias removidas com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover parcelas intermediárias: " + e.getMessage(), e);
        }
    }

    // Método para adicionar apenas as 5 primeiras e as 5 últimas parcelas
    public void adicionarParcelasLimitadas(List<Parcelas> parcelas) {
        if (parcelas.isEmpty()) {
            System.out.println("Nenhuma parcela para adicionar.");
            return;
        }

        int financiamentoId = parcelas.get(0).getFinanciamentoId(); // Assume que todas as parcelas são do mesmo financiamento

        // Adiciona todas as parcelas temporariamente
        for (Parcelas parcela : parcelas) {
            adicionarParcela(parcela);
        }

        // Remove as parcelas intermediárias
        removerParcelasIntermediarias(financiamentoId);
    }

    // Método para listar todas as parcelas
    public List<Parcelas> listarParcelas() {
        List<Parcelas> parcelas = new ArrayList<>();
        String sql = "SELECT * FROM parcelas";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Parcelas parcela = new Parcelas(
                        rs.getInt("id"),
                        rs.getInt("financiamento_id"),
                        rs.getInt("numero_parcela"),
                        rs.getDouble("valor_parcela"),
                        rs.getDouble("valor_amortizacao")
                );
                parcelas.add(parcela);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar parcelas: " + e.getMessage(), e);
        }
        return parcelas;
    }

    // Método para fechar a conexão
    public void fecharConexao() {
        try {
            if (conexao != null) {
                conexao.close();
                System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão: " + e.getMessage(), e);
        }
    }
}