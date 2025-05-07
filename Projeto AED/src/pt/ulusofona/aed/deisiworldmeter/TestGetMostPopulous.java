package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestGetMostPopulous {
    @Test
    public void getMostPopulous_OneResult() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MOST_POPULOUS 1");

        assertEquals("Alemanha:dubai:1137376\n", result.result);

    }
    @Test
    public void getMostPopulous_FiveResults() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MOST_POPULOUS 5");

        assertEquals("Alemanha:dubai:1137376\n" +
                "Afeganistão:achu:543942\n" +
                "Portugal:almada:20430\n" +
                "Albânia:les escaldes:15854\n" +
                "Andorra:sant julia de loria:8020\n", result.result);

    }@Test
    public void getMostPopulous_SomeResults() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MOST_POPULOUS 3");
        assertEquals("Alemanha:dubai:1137376\n" +
                "Afeganistão:achu:543942\n" +
                "Portugal:almada:20430\n", result.result);

    }
    @Test
    public void getMostPopulous_defesa() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_MOST_POPULOUS 5 ola");

        assertEquals("Alemanha:dubai:1137376\n" +
                "Afeganistão:achu:543942\n" +
                "Portugal:almada:20430\n" +
                "Albânia:les escaldes:15854\n" +
                "Andorra:sant julia de loria:8020\n", result.result);

    }
}