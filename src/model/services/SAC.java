package model.services;

import model.entities.Financiamento;
import model.entities.Parcelas;

import java.util.ArrayList;
import java.util.List;

public class SAC implements Amortizacao {

    @Override
    public double calcularParcela(double valorFinanciado, double taxaJuros, int prazo, int numeroParcela) {
        double amortizacao = valorFinanciado / prazo;
        double saldoDevedor = valorFinanciado - (amortizacao * (numeroParcela - 1));
        double juros = saldoDevedor * (taxaJuros / 12);
        return amortizacao + juros;
    }

    @Override
    public List<Parcelas> calcularParcelas(Financiamento financiamento) {
        List<Parcelas> parcelas = new ArrayList<>();
        double amortizacao = financiamento.getValorFinanciado() / financiamento.getPrazo();

        for (int i = 1; i <= financiamento.getPrazo(); i++) {
            double saldoDevedor = financiamento.getValorFinanciado() - (amortizacao * (i - 1));
            double juros = saldoDevedor * (financiamento.getTaxaJuros() / 12);
            double valorParcela = amortizacao + juros;

            Parcelas parcela = new Parcelas(i, financiamento.getId(), i, valorParcela, amortizacao);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}