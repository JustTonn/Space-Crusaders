package entity;

public class Perk {
    public String nome;
    public String descricao;
    public tipoPerk tipo;

    public Perk(String nome, String descricao, tipoPerk tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
    }
}
