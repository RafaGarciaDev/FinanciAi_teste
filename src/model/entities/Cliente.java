package model.entities;

public class Cliente {
    private int id;
    private String nome;
    private double rendaMensal;

    // Construtores
    public Cliente() {}

    public Cliente(int id, String nome, double rendaMensal) {
        this.id = id;
        this.nome = nome;
        this.rendaMensal = rendaMensal;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(double rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", rendaMensal=" + rendaMensal +
                '}';
    }
}