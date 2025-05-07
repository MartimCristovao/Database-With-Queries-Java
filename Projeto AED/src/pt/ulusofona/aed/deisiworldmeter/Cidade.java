package pt.ulusofona.aed.deisiworldmeter;

import java.util.ArrayList;

public class Cidade {
    String alfa2;
    String nome;
    String regiao;
    double populacao;
    String coordenadas;


    public Cidade(String alfa2, String nome, String regiao, double populacao, double latitude, double longitude) {
        this.alfa2 = alfa2;
        this.nome = nome;
        this.regiao = regiao;
        this.populacao = populacao;
        this.coordenadas = "(" + latitude + "," + longitude + ")";
    }

    @Override
    public String toString() {
        int populacaoInt = (int) populacao;
        return nome + " | " + alfa2.toUpperCase() + " | " + regiao + " | " + populacaoInt + " | " + coordenadas;
    }
}

