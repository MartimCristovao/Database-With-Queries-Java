package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestGetTopCitiesByCountry {
    @Test
    public void getTopCitiesByCountry_OneResult() {

        Main.parseFiles(new File("test-files/TopCitiesByCountry"));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY 1 Portugal");

        assertEquals("almada:20K\n", result.result);

    }
    @Test
    public void getTopCitiesByCountry_NoSamePopualation() {

        Main.parseFiles(new File("test-files/TopCitiesByCountry"));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY 3 Alemanha");

        assertEquals("dubai:1137K\n" +
                "abu dhabi:603K\n" +
                "sharjah:543K\n", result.result);

    }
    @Test
    public void getTopCitiesByCountry_SamePopualation() {

        Main.parseFiles(new File("test-files/TopCitiesByCountry"));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY 3 Portugal");

        assertEquals("almada:20K\n" +
                "seixal:20K\n" +
                "porto:11K\n", result.result);

    }
    @Test
    public void getTopCitiesByCountry_ByPassFlag() {

        Main.parseFiles(new File("test-files/TopCitiesByCountry"));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY -1 Portugal");

        assertEquals("almada:20K\n" +
                "seixal:20K\n" +
                "porto:11K\n", result.result);

    }
    @Test
    public void getTopCitiesByCountry_defesa() {

        Main.parseFiles(new File("test-files/TopCitiesByCountry"));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY 3 Portugal");

        assertEquals("almada:20K\n" +
                "seixal:20K\n" +
                "porto:11K\n", result.result);

    }
}