package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestGetHistory {
    @Test
    public void getHistory_AllYears_OneYear() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_HISTORY 2024 2024 Albânia");

        assertEquals("2024:1128k:1222k\n", result.result);

    }
    @Test
    public void getHistory_AllYears_OneYear_WrongYears() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_HISTORY 2020 2025 Albânia");

        assertEquals("2024:1128k:1222k\n", result.result);

    }
    @Test
    public void getHistory_SomeYears_MultipleYears_() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_HISTORY 1954 2023 Andorra");

        assertEquals("2022:1225k:1318k\n" +
                "2023:1250k:1343k\n", result.result);

    }

    @Test
    public void getHistory_AllYears_MultipleYears() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_HISTORY 1954 2024 Andorra");

        assertEquals("2022:1225k:1318k\n" +
                "2023:1250k:1343k\n" +
                "2024:1275k:1367k\n", result.result);

    }
    @Test
    public void getHistory_AllYears_defesa() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_HISTORY 1954 2024 Andorra");

        assertEquals("2022:1225k:1318k\n" +
                "2023:1250k:1343k\n" +
                "2024:1275k:1367k\n", result.result);

    }
}