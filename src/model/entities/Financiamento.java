package model.entities;

import model.enums.TipoAmortizacao;

import java.time.LocalDate;

public class Financiamento {
    private int id;
    private int clienteId;
    private int imovelId;
    private double valorFinanciado;
    private double taxaJuros;
    private double valorEntrada;
    private int prazo;
    private TipoAmortizacao tipoAmortizacao;
    private double totalPagar;
    private LocalDate dataSimulacao;

    // Construtores
    public Financiamento() {}

    public Financiamento(int id, int clienteId, int imovelId, double valorFinanciado, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao, double totalPagar, LocalDate dataSimulacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.imovelId = imovelId;
        this.valorFinanciado = valorFinanciado;
        this.taxaJuros = taxaJuros;
        this.valorEntrada = valorEntrada;
        this.prazo = prazo;
        this.tipoAmortizacao = tipoAmortizacao;
        this.totalPagar = totalPagar;
        this.dataSimulacao = dataSimulacao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getImovelId() {
        return imovelId;
    }

    public void setImovelId(int imovelId) {
        this.imovelId = imovelId;
    }

    public double getValorFinanciado() {
        return valorFinanciado;
    }

    public void setValorFinanciado(double valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    public double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public TipoAmortizacao getTipoAmortizacao() {
        return tipoAmortizacao;
    }

    public void setTipoAmortizacao(TipoAmortizacao tipoAmortizacao) {
        this.tipoAmortizacao = tipoAmortizacao;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public LocalDate getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDate dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }

    @Override
    public String toString() {
        return String.format(
                "Financiamento [ID: %d | Cliente ID: %d | Imóvel ID: %d | Valor Financiado: R$ %.2f | Taxa de Juros: %.2f%% | Valor Entrada: R$ %.2f | Prazo: %d meses | Tipo Amortização: %s | Total a Pagar: R$ %.2f | Data Simulação: %s]",
                id, clienteId, imovelId, valorFinanciado, taxaJuros, valorEntrada, prazo, tipoAmortizacao, totalPagar, dataSimulacao
        );
    }
}