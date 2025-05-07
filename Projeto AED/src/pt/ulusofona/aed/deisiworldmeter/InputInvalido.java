package pt.ulusofona.aed.deisiworldmeter;

public class InputInvalido {
    String nomeFicheiro;
    int linhasValidas;
    int linhasInvalidas;
    int primeiraLinhaInvalida;

    public InputInvalido(String nomeFicheiro, int linhasValidas, int linhasInvalidas, int primeiraLinhaInvalida) {
        this.nomeFicheiro = nomeFicheiro;
        this.linhasValidas = linhasValidas;
        this.linhasInvalidas = linhasInvalidas;
        this.primeiraLinhaInvalida = primeiraLinhaInvalida;
    }

    public InputInvalido(String nomeFicheiro) {
        this.nomeFicheiro = nomeFicheiro;
        this.linhasValidas = 0;
        this.linhasInvalidas = 0;
        this.primeiraLinhaInvalida = -1; // Inicializado com -1 para indicar que ainda não foi encontrada uma linha inválida.
    }

    @Override
    public String toString() {
        return nomeFicheiro + " | " + linhasValidas + " | " + linhasInvalidas + " | " + primeiraLinhaInvalida;
    }
}