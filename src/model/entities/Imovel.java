package model.entities;

import model.enums.TipoImovel;

public class Imovel {
    private int id;
    private double valor;
    private TipoImovel tipoImovel;

    // Construtores
    public Imovel() {}

    public Imovel(int id, double valor, TipoImovel tipoImovel) {
        this.id = id;
        this.valor = valor;
        this.tipoImovel = tipoImovel;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getvalor() {
        return valor;
    }

    public void setvalor(double valor) {
        this.valor = valor;
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
                ", valor=" + valor +
                ", tipoImovel=" + tipoImovel +
                '}';
    }


}