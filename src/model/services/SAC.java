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
        double juros = saldoDevedor * (taxaJuros / 12 / 100); // Converte a taxa de juros para decimal
        double valorParcela = amortizacao + juros;
        return Math.round(valorParcela * 100.0) / 100.0; // Arredonda para 2 casas decimais
    }

    @Override
    public List<Parcelas> calcularParcelas(Financiamento financiamento) {
        List<Parcelas> parcelas = new ArrayList<>();
        double amortizacao = financiamento.getValorFinanciado() / financiamento.getPrazo();
        double taxaMensal = financiamento.getTaxaJuros() / 12 / 100; // Converte a taxa de juros para decimal

        for (int i = 1; i <= financiamento.getPrazo(); i++) {
            double saldoDevedor = financiamento.getValorFinanciado() - (amortizacao * (i - 1));
            double juros = saldoDevedor * taxaMensal;
            double valorParcela = amortizacao + juros;

            // Arredonda os valores para 2 casas decimais
            juros = Math.round(juros * 100.0) / 100.0;
            valorParcela = Math.round(valorParcela * 100.0) / 100.0;

            Parcelas parcela = new Parcelas(i, financiamento.getId(), i, valorParcela, amortizacao);
            parcelas.add(parcela);
        }

        return parcelas;
    }
}