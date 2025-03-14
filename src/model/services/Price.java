package model.services;

import model.entities.Financiamento;
import model.entities.Parcelas;

import java.util.ArrayList;
import java.util.List;

public class Price implements Amortizacao {

    @Override
    public double calcularParcela(double valorFinanciado, double taxaJuros, int prazo, int numeroParcela) {
        double taxaMensal = taxaJuros / 12;
        double valorParcela = valorFinanciado * (taxaMensal / (1 - Math.pow(1 + taxaMensal, -prazo)));
        return valorParcela;
    }

    @Override
    public List<Parcelas> calcularParcelas(Financiamento financiamento) {
        List<Parcelas> parcelas = new ArrayList<>();
        double saldoDevedor = financiamento.getValorFinanciado();
        double taxaMensal = financiamento.getTaxaJuros() / 12;
        double valorParcela = calcularParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo(), 1);

        for (int i = 1; i <= financiamento.getPrazo(); i++) {
            double juros = saldoDevedor * taxaMensal;
            double amortizacao = valorParcela - juros;
            saldoDevedor -= amortizacao;

            Parcelas parcela = new Parcelas(i, financiamento.getId(), i, valorParcela, amortizacao);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}