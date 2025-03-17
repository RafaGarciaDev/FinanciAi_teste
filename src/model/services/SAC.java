package model.services;

import model.entities.Financiamento;
import model.entities.Parcelas;

import java.util.ArrayList;
import java.util.List;

public class SAC implements Amortizacao {

    // Valores típicos de seguros no Brasil
    private static final double TAXA_MIP = 0.0003; // Exemplo: 0,03% do saldo devedor
    private static final double TAXA_DFI = 0.0001; // Exemplo: 0,01% do valor financiado

    @Override
    public double calcularParcela(double valorFinanciado, double taxaJuros, int prazo, int numeroParcela) {
        double taxaMensal = taxaJuros / 12 / 100; // Converte a taxa anual para mensal em decimal
        double amortizacao = valorFinanciado / prazo; // Amortização constante no SAC
        double juros = (valorFinanciado - (amortizacao * (numeroParcela - 1))) * taxaMensal;
        return Math.round((amortizacao + juros) * 100.0) / 100.0;
    }

    @Override
    public List<Parcelas> calcularParcelas(Financiamento financiamento) {
        List<Parcelas> parcelas = new ArrayList<>();

        double saldoDevedor = financiamento.getValorFinanciado();
        double taxaMensal = financiamento.getTaxaJuros() / 12 / 100;
        double amortizacaoConstante = saldoDevedor / financiamento.getPrazo();

        for (int i = 1; i <= financiamento.getPrazo(); i++) {
            double juros = saldoDevedor * taxaMensal;
            double valorParcelaBase = amortizacaoConstante + juros;

            // Cálculo do seguro MIP (sobre saldo devedor) e DFI (sobre valor financiado)
            double valorMIP = saldoDevedor * TAXA_MIP;
            double valorDFI = financiamento.getValorFinanciado() * TAXA_DFI;

            double valorParcelaTotal = valorParcelaBase + valorMIP + valorDFI;

            saldoDevedor -= amortizacaoConstante;

            // Arredondamento para 2 casas decimais
            juros = Math.round(juros * 100.0) / 100.0;
            amortizacaoConstante = Math.round(amortizacaoConstante * 100.0) / 100.0;
            saldoDevedor = Math.round(saldoDevedor * 100.0) / 100.0;
            valorMIP = Math.round(valorMIP * 100.0) / 100.0;
            valorDFI = Math.round(valorDFI * 100.0) / 100.0;
            valorParcelaTotal = Math.round(valorParcelaTotal * 100.0) / 100.0;

            Parcelas parcela = new Parcelas(i, financiamento.getId(), i, valorParcelaTotal, amortizacaoConstante);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}
