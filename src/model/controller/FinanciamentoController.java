package model.controller;

import dao.FinanciamentoDAO;
import dao.ParcelasDAO;
import model.entities.Financiamento;
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

    public FinanciamentoController() {
        this.financiamentoDAO = new FinanciamentoDAO();
        this.parcelasDAO = new ParcelasDAO();
    }

    public void simularFinanciamento(int id, int clienteId, int imovelId, double valorFinanciado, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
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

        System.out.println("Financiamento simulado e salvo com sucesso!");
    }

    // Método para calcular o total a pagar
    private double calcularTotalPagar(double valorFinanciado, double taxaJuros, int prazo, Amortizacao amortizacao) {
        double totalPagar = 0;
        for (int i = 1; i <= prazo; i++) {
            double valorParcela = amortizacao.calcularParcela(valorFinanciado, taxaJuros, prazo, i);
            totalPagar += valorParcela;
        }
        return totalPagar;
    }

    // Método para listar todos os financiamentos
    public void listarFinanciamentos() {
        System.out.println("Lista de Financiamentos:");
        financiamentoDAO.listarFinanciamentos().forEach(System.out::println);
    }

    // Método para listar todas as parcelas de um financiamento
    public void listarParcelas(int financiamentoId) {
        System.out.println("Parcelas do Financiamento ID " + financiamentoId + ":");
        parcelasDAO.listarParcelas().stream()
                .filter(p -> p.getFinanciamentoId() == financiamentoId)
                .forEach(System.out::println);
    }

    // Método para fechar as conexões
    public void fecharConexoes() {
        financiamentoDAO.fecharConexao();
        parcelasDAO.fecharConexao();
    }
}