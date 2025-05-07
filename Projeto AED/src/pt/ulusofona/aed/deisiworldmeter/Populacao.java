package pt.ulusofona.aed.deisiworldmeter;

public class Populacao {
    int id;
    int ano;
    int populacaoM;
    int populacaoF;
    Double densidade;

    public Populacao(int id, int ano, int populacaoM, int populacaoF, Double densidade) {
        this.id = id;
        this.ano = ano;
        this.populacaoM = populacaoM;
        this.populacaoF = populacaoF;
        this.densidade = densidade;
    }

}

