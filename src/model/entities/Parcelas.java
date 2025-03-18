package model.entities;

public class Parcelas {
    private int id;
    private int financiamentoId;
    private int numeroParcela;
    private double valorParcela;
    private double valorAmortizacao;

    // Construtores
    public Parcelas() {}

    public Parcelas(int id, int financiamentoId, int numeroParcela, double valorParcela, double valorAmortizacao) {
        this.id = id;
        this.financiamentoId = financiamentoId;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinanciamentoId() {
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public double getValorAmortizacao() {
        return valorAmortizacao;
    }

    public void setValorAmortizacao(double valorAmortizacao) {
        this.valorAmortizacao = valorAmortizacao;
    }

    @Override
    public String toString() {
        return "Parcelas{" +
                "id=" + id +
                ", financiamentoId=" + financiamentoId +
                ", numeroParcela=" + numeroParcela +
                ", valorParcela=" + valorParcela +
                ", valorAmortizacao=" + valorAmortizacao +
                '}';
    }
}