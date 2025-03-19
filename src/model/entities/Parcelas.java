package model.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

    // Método para calcular os juros
    public double getValorJuros() {
        return valorParcela - valorAmortizacao;
    }

    // Método para formatar valores com vírgula como separador decimal
    private String formatarValor(double valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        return df.format(valor);
    }

    @Override
    public String toString() {
        return String.format(
                "| %-11d | %-12s | %-17s | %-12s |",
                numeroParcela, formatarValor(valorParcela), formatarValor(valorAmortizacao), formatarValor(getValorJuros())
        );
    }


}