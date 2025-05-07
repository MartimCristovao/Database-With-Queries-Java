package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestCountCities {

    @Test
    public void countCities_NoCities() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("COUNT_CITIES 1000000000");

        assertEquals("0",result.result);

    }
    @Test
    public void countCities_AllCities() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("COUNT_CITIES 1");

        assertEquals("12",result.result);

    }
    @Test
    public void countCities_SomeCities() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("COUNT_CITIES 15000");

        assertEquals("7",result.result);

    }
    @Test
    public void countCities_defesa() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("COUNT_CITIES 15000");

        assertEquals("7",result.result);

    }
}