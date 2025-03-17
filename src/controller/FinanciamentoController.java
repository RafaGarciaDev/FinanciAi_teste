package controller;

import dao.*;
import gerador_pdf_do_financiamento.GeradorPDF;
import model.entities.Cliente;
import model.entities.Financiamento;
import model.entities.Imovel;
import model.entities.Parcelas;
import model.enums.TipoAmortizacao;
import model.services.Amortizacao;
import model.services.Price;
import model.services.SAC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FinanciamentoController {

    private FinanciamentoDAO financiamentoDAO;
    private ParcelasDAO parcelasDAO;
    private ClienteDAO clienteDAO;
    private ImovelDAO imovelDAO;

    public FinanciamentoController() {
        this.financiamentoDAO = new FinanciamentoDAO();
        this.parcelasDAO = new ParcelasDAO();
        this.clienteDAO = new ClienteDAO();
        this.imovelDAO = new ImovelDAO();
    }

    // Método para gerar um novo ID de financiamento
    public int gerarNovoIdFinanciamento() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM financiamentos"; // Consulta o maior ID existente
        int novoId = 1; // Valor padrão caso não haja financiamentos cadastrados

        try (Connection conn = Conexao.conectar(); // Obtém a conexão com o banco de dados
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                // Se houver financiamentos cadastrados, incrementa o maior ID em 1
                novoId = rs.getInt("max_id") + 1;
            }
        }

        return novoId;
    }

    // Método para simular um financiamento
    public void simularFinanciamento(int id, int clienteId, int imovelId, double valorTotalImovel, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // Inicia a transação

            // Recuperar informações do cliente e do imóvel
            Cliente cliente = clienteDAO.buscarClientePorId(clienteId);
            Imovel imovel = imovelDAO.buscarImovelPorId(imovelId);

            // Verifica se o cliente e o imóvel foram encontrados
            if (cliente == null) {
                throw new RuntimeException("Cliente não encontrado com o ID: " + clienteId);
            }
            if (imovel == null) {
                throw new RuntimeException("Imóvel não encontrado com o ID: " + imovelId);
            }

            // Calcular o valor financiado (valor total do imóvel menos a entrada)
            double valorFinanciado = valorTotalImovel - valorEntrada;

            // Calcular o total a pagar e as parcelas
            Amortizacao amortizacao;
            if (tipoAmortizacao == TipoAmortizacao.PRICE) {
                amortizacao = new Price();
            } else {
                amortizacao = new SAC();
            }

            double totalPagar = calcularTotalPagar(valorFinanciado, taxaJuros, prazo, amortizacao);
            LocalDate dataSimulacao = LocalDate.now();

            // Criar o financiamento
            Financiamento financiamento = new Financiamento(id, clienteId, imovelId, valorFinanciado, taxaJuros, valorEntrada, prazo, tipoAmortizacao, totalPagar, dataSimulacao);

            // Salvar o financiamento no banco de dados
            financiamentoDAO.adicionarFinanciamento(financiamento);

            // Calcular as parcelas
            List<Parcelas> parcelas = amortizacao.calcularParcelas(financiamento);

            // Salvar apenas as 5 primeiras e as 5 últimas parcelas
            parcelasDAO.adicionarParcelasLimitadas(parcelas);

            // Commit da transação
            conn.commit();

            // Gerar o PDF com todas as parcelas e informações do cliente/imóvel
            GeradorPDF.gerarPDF(
                    financiamento.getId(),
                    cliente.getNome(),
                    valorTotalImovel,
                    valorEntrada,
                    imovel.getTipoImovel(),
                    valorFinanciado,
                    totalPagar,
                    totalPagar - valorFinanciado,
                    totalPagar,
                    totalPagar - valorFinanciado,
                    parcelas
            );

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Erro ao simular financiamento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura o modo de autocommit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Método para calcular o total a pagar
    private double calcularTotalPagar(double valorFinanciado, double taxaJuros, int prazo, Amortizacao amortizacao) {
        double totalPagar = 0;
        try {
            for (int i = 1; i <= prazo; i++) {
                double valorParcela = amortizacao.calcularParcela(valorFinanciado, taxaJuros, prazo, i);
                totalPagar += valorParcela;
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular o total a pagar: " + e.getMessage());
            e.printStackTrace();
        }
        return totalPagar;
    }

    // Método para listar todos os financiamentos
    public void listarFinanciamentos() {
        try {
            System.out.println("Lista de Financiamentos:");
            financiamentoDAO.listarFinanciamentos().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erro ao listar financiamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para listar todas as parcelas de um financiamento
    public void listarParcelas(int financiamentoId) {
        try {
            System.out.println("Parcelas do Financiamento ID " + financiamentoId + ":");
            parcelasDAO.listarParcelas().stream()
                    .filter(p -> p.getFinanciamentoId() == financiamentoId)
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erro ao listar parcelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para fechar as conexões
    public void fecharConexoes() {
        try {
            financiamentoDAO.fecharConexao();
            parcelasDAO.fecharConexao();
            clienteDAO.fecharConexao();
            imovelDAO.fecharConexao();
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }
}