package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGetDensityBelow {
    @Test
    public void getDensityBelow_AllResults() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_DENSITY_BELOW 999999 Portugal");

        assertEquals("2024 - 86.8637\n" +
                "2020 - 88.7571\n",result.result);

    }
    @Test
    public void getDensityBelow_SomeResults() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_DENSITY_BELOW 100 Andorra");

        assertEquals("2023 - 99.9553\n" +
                "2022 - 98.0446\n",result.result);

    }
    @Test
    public void getDensityBelow_NoCountry() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_DENSITY_BELOW 999999 alalala");

        assertEquals("Sem resultados",result.result);

    }
    @Test
    public void getDensityBelow_ZeroDensity() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_DENSITY_BELOW 0 Portugal");

        assertEquals("Sem resultados",result.result);

    }
    @Test
    public void getDensityBelow_defesa() {

        Main.parseFiles(new File("test-files/Queries"));
        Result result = Main.execute("GET_DENSITY_BELOW 103 Andorra");

        assertEquals("2023 - 99.9553\n" +
                "2022 - 98.0446\n",result.result);

    }


}
