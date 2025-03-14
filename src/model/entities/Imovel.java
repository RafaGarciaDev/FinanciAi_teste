package model.entities;

import model.enums.TipoImovel;

public class Imovel {
    private int id;
    private double valorMercado;
    private TipoImovel tipoImovel;

    // Construtores
    public Imovel() {}

    public Imovel(int id, double valorMercado, TipoImovel tipoImovel) {
        this.id = id;
        this.valorMercado = valorMercado;
        this.tipoImovel = tipoImovel;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorMercado() {
        return valorMercado;
    }

    public void setValorMercado(double valorMercado) {
        this.valorMercado = valorMercado;
    }

    public TipoImovel getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(TipoImovel tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    @Override
    public String toString() {
        return "Imovel{" +
                "id=" + id +
                ", valorMercado=" + valorMercado +
                ", tipoImovel=" + tipoImovel +
                '}';
    }
}