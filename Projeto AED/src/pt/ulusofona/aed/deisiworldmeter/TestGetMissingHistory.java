package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestGetMissingHistory {
    @Test
    public void getMissingHistory_NothingMissing() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MISSING_HISTORY 2024 2024");

        assertEquals("Sem resultados", result.result);

    }
    @Test
    public void getMissingHistory_AllMissing() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MISSING_HISTORY 2021 2024");

        assertEquals("ad:Andorra\n" +
                "af:Afeganistão\n" +
                "ae:Alemanha\n" +
                "al:Albânia\n" +
                "pt:Portugal\n", result.result);

    }
    @Test
    public void getMissingHistory_SomeMissing() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MISSING_HISTORY 2022 2024");

        assertEquals("af:Afeganistão\n" +
                "ae:Alemanha\n" +
                "al:Albânia\n" +
                "pt:Portugal\n", result.result);

    }
    @Test
    public void getMissingHistory_defesa() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MISSING_HISTORY 2022 2024");

        assertEquals("af:Afeganistão\n" +
                "ae:Alemanha\n" +
                "al:Albânia\n" +
                "pt:Portugal\n", result.result);

    }
}