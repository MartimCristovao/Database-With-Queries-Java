package pt.ulusofona.aed.deisiworldmeter;

import java.util.ArrayList;

class Pais {
    int id;
    String nome;
    String alfa2;
    String alfa3;



    public Pais(int id, String alfa2, String alfa3, String nome) {
        this.id = id;
        this.alfa2 = alfa2;
        this.alfa3 = alfa3;
        this.nome = nome;
    }

    public Pais() {

    }

    @Override
    public String toString() {

        if(id <= 700){
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase();
        }else{
            int contagemOcorrencias = 0;
            for (Populacao populacao : Main.populacaoT) {
                if (populacao.id == id) {
                    contagemOcorrencias++;
                }
            }
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase() + " | "
                    + contagemOcorrencias;
        }


    }

}



