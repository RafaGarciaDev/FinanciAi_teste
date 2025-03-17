package controller;

import dao.ClienteDAO;
import dao.ImovelDAO;
import dao.FinanciamentoDAO;
import dao.ParcelasDAO;
import gerador_pdf_do_financiamento.GeradorPDF;
import model.entities.Cliente;
import model.entities.Financiamento;
import model.entities.Imovel;
import model.entities.Parcelas;
import model.enums.TipoAmortizacao;
import model.services.Amortizacao;
import model.services.Price;
import model.services.SAC;
import java.time.LocalDate;
import java.util.List;

public class FinanciamentoController {

    private FinanciamentoDAO financiamentoDAO;
    private ParcelasDAO parcelasDAO;
    private ClienteDAO clienteDAO; // Declaração do ClienteDAO
    private ImovelDAO imovelDAO;   // Declaração do ImovelDAO

    public FinanciamentoController() {
        this.financiamentoDAO = new FinanciamentoDAO();
        this.parcelasDAO = new ParcelasDAO();
        this.clienteDAO = new ClienteDAO(); // Inicialização do ClienteDAO
        this.imovelDAO = new ImovelDAO();   // Inicialização do ImovelDAO
    }

    public void simularFinanciamento(int id, int clienteId, int imovelId, double valorTotalImovel, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        try {
            // Recuperar informações do cliente e do imóvel
            Cliente cliente = clienteDAO.buscarClientePorId(clienteId); // Método para buscar cliente por ID
            Imovel imovel = imovelDAO.buscarImovelPorId(imovelId); // Método para buscar imóvel por ID

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
            parcelasDAO.adicionarParcelasLimitadas(parcelas); // Passa o total de parcelas

            // Gerar o PDF com todas as parcelas e informações do cliente/imóvel
            GeradorPDF.gerarPDF(
                    financiamento.getId(),   // ID do financiamento
                    cliente.getNome(),       // Nome do cliente
                    valorTotalImovel,        // Valor total do imóvel
                    valorEntrada,            // Valor de entrada
                    imovel.getTipoImovel(),  // Tipo de imóvel (CASA ou APARTAMENTO)
                    valorFinanciado,         // Valor financiado (valorTotalImovel - valorEntrada)
                    totalPagar,              // Total a pagar no sistema Price
                    totalPagar - valorFinanciado, // Juros totais no sistema Price
                    totalPagar,              // Total a pagar no sistema SAC
                    totalPagar - valorFinanciado, // Juros totais no sistema SAC
                    parcelas                 // Lista de parcelas
            );

            //System.out.println("PDF gerado com sucesso: SimulacaoFinanciamento_" + financiamento.getId() + ".pdf");

        } catch (Exception e) {
            System.err.println("Erro ao simular financiamento: " + e.getMessage());
            e.printStackTrace();
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
            clienteDAO.fecharConexao(); // Fechar conexão do ClienteDAO
            imovelDAO.fecharConexao();  // Fechar conexão do ImovelDAO
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }
}