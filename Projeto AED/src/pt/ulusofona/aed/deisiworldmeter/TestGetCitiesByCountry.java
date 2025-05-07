package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


public class TestGetCitiesByCountry {

    @Test
    public void getCitiesByCountry_NoCities() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 0 Portugal");

        assertEquals("", result.result);

    }
    @Test
    public void getCitiesByCountry_AllCities() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 4 Portugal");

        assertEquals("almada\n" +
                "seixal\n" +
                "porto\n" +
                "coimbra\n", result.result);

    }
    @Test
    public void getCitiesByCountry_SomeCities() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 2 Alemanha");

        assertEquals("abu dhabi\n" +
                "dubai\n",result.result);

    }
    @Test
    public void getCitiesByCountry_MoreCitiesThanExist() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 5 Albânia");

        assertEquals("les escaldes\n" +
                "ordino\n",result.result);

    }
    @Test
    public void getCitiesByCountry_NoCountry() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 4 Nenhum");

        assertEquals("Pais invalido: Nenhum", result.result);

    }
    @Test
    public void getCitiesByCountry_defesa() {
        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 4 Portugal");

        assertEquals("almada\n" +
                "seixal\n" +
                "porto\n" +
                "coimbra\n", result.result);

    }

}