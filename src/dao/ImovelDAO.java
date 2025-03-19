package dao;

import model.entities.Imovel;
import model.enums.TipoImovel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImovelDAO {
    private Connection conexao;

    public ImovelDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaImoveis(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela imoveis se não existir
    private void criarTabelaImoveis() {
        String verificaTabela = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'imoveis'";
        String criaTabela = "CREATE TABLE IF NOT EXISTS imoveis (" +
                "id INT PRIMARY KEY, " + // Removido AUTO_INCREMENT
                "valor DOUBLE NOT NULL, " +
                "tipo_imovel VARCHAR(50) NOT NULL)";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(verificaTabela)) {

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Tabela 'imoveis' já existe.");
            } else {
                stmt.executeUpdate(criaTabela);
                System.out.println("Tabela 'imoveis' criada com sucesso!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar tabela 'imoveis': " + e.getMessage(), e);
        }
    }

    // Método para verificar se um imóvel já existe
    private boolean imovelExiste(int id) {
        String sql = "SELECT COUNT(*) FROM imoveis WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se imóvel existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para adicionar um imóvel
    public void adicionarImovel(Imovel imovel) {
        if (imovelExiste(imovel.getId())) {
            System.out.println("Imóvel com ID " + imovel.getId() + " já existe no banco de dados.");
            return;
        }

        String sql = "INSERT INTO imoveis (id, valor, tipo_imovel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, imovel.getId());
            stmt.setDouble(2, imovel.getvalor());
            stmt.setString(3, imovel.getTipoImovel().toString());
            stmt.executeUpdate();
            System.out.println("Imóvel adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar imóvel: " + e.getMessage(), e);
        }
    }

    // Método para listar todos os imóveis
    public List<Imovel> listarImoveis() {
        List<Imovel> imoveis = new ArrayList<>();
        String sql = "SELECT * FROM imoveis";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Imovel imovel = new Imovel(
                        rs.getInt("id"),
                        rs.getDouble("valor"),
                        TipoImovel.valueOf(rs.getString("tipo_imovel"))
                );
                imoveis.add(imovel);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar imóveis: " + e.getMessage(), e);
        }
        return imoveis;
    }

    // Método para buscar um imóvel por ID
    public Imovel buscarImovelPorId(int id) {
        String sql = "SELECT * FROM imoveis WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Imovel(
                            rs.getInt("id"),
                            rs.getDouble("valor"),
                            TipoImovel.valueOf(rs.getString("tipo_imovel"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar imóvel por ID: " + e.getMessage(), e);
        }
        return null; // Retorna null se o imóvel não for encontrado
    }

    // Método para fechar a conexão
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão: " + e.getMessage(), e);
        }
    }
}