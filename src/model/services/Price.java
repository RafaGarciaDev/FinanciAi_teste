package model.services;

import model.entities.Financiamento;
import model.entities.Parcelas;

import java.util.ArrayList;
import java.util.List;

public class Price implements Amortizacao {

    // Valores típicos de seguros no Brasil
    private static final double TAXA_MIP = 0.0003; // Exemplo: 0,03% do saldo devedor
    private static final double TAXA_DFI = 0.0001; // Exemplo: 0,01% do valor financiado

    @Override
    public double calcularParcela(double valorFinanciado, double taxaJuros, int prazo, int numeroParcela) {
        double taxaMensal = taxaJuros / 12 / 100; // Converte a taxa anual para mensal em decimal
        return Math.round(valorFinanciado * (taxaMensal / (1 - Math.pow(1 + taxaMensal, -prazo))) * 100.0) / 100.0;
    }

    @Override
    public List<Parcelas> calcularParcelas(Financiamento financiamento) {
        List<Parcelas> parcelas = new ArrayList<>();

        double saldoDevedor = financiamento.getValorFinanciado();
        double taxaMensal = financiamento.getTaxaJuros() / 12 / 100;
        double valorParcelaBase = calcularParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo(), 1);

        for (int i = 1; i <= financiamento.getPrazo(); i++) {
            double juros = saldoDevedor * taxaMensal;
            double amortizacao = valorParcelaBase - juros;

            // Cálculo do seguro MIP (sobre saldo devedor) e DFI (sobre valor financiado)
            double valorMIP = saldoDevedor * TAXA_MIP;
            double valorDFI = financiamento.getValorFinanciado() * TAXA_DFI;

            double valorParcelaTotal = valorParcelaBase + valorMIP + valorDFI;

            saldoDevedor -= amortizacao;

            // Arredondamento para 2 casas decimais
            juros = Math.round(juros * 100.0) / 100.0;
            amortizacao = Math.round(amortizacao * 100.0) / 100.0;
            saldoDevedor = Math.round(saldoDevedor * 100.0) / 100.0;
            valorMIP = Math.round(valorMIP * 100.0) / 100.0;
            valorDFI = Math.round(valorDFI * 100.0) / 100.0;
            valorParcelaTotal = Math.round(valorParcelaTotal * 100.0) / 100.0;

            Parcelas parcela = new Parcelas(i, financiamento.getId(), i, valorParcelaTotal, amortizacao);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}