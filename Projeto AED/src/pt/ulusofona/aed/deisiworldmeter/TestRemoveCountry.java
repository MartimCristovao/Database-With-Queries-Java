package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestRemoveCountry {
    @Test
    public void removeCountry_ThatExists() {

        Main.parseFiles(new File("test-files/RemoveCountry"));
        Result result = Main.execute("REMOVE_COUNTRY Portugal");

        StringBuilder paisesLista = new StringBuilder();
        for(Pais pais : Main.paisesT){
            paisesLista.append(pais.nome).append('\n');
        }
        assertEquals("Alemanha\n" +
                "Albânia\n" +
                "Andorra\n" +
                "Afeganistão\n", paisesLista.toString());

    }
    @Test
    public void removeCountry_Cidades() {

        Main.parseFiles(new File("test-files/RemoveCountry"));
        int tamanhoInicial = Main.getObjects(TipoEntidade.CIDADE).size();
        Result result = Main.execute("REMOVE_COUNTRY Portugal");
        int tamanhoFinal = Main.getObjects(TipoEntidade.CIDADE).size();

        assertEquals(4, tamanhoInicial-tamanhoFinal);

    }
    @Test
    public void removeCountry_DontExists() {

        Main.parseFiles(new File("test-files/RemoveCountry"));
        Result result = Main.execute("REMOVE_COUNTRY Birl");

        assertEquals("Pais invalido", result.result);
    }
    @Test
    public void removeCountry_defesa() {

        Main.parseFiles(new File("test-files/RemoveCountry"));
        int tamanhoInicial = Main.getObjects(TipoEntidade.CIDADE).size();
        Result result = Main.execute("REMOVE_COUNTRY Portugal");
        int tamanhoFinal = Main.getObjects(TipoEntidade.CIDADE).size();

        assertEquals(4, tamanhoInicial-tamanhoFinal);

    }
}