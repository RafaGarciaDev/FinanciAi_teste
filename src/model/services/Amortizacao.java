package model.services;

import model.entities.Financiamento;
import model.entities.Parcelas;

import java.util.List;

public interface Amortizacao {
    double calcularParcela(double valorFinanciado, double taxaJuros, int prazo, int numeroParcela);
    List<Parcelas> calcularParcelas(Financiamento financiamento);
}