package pt.ulusofona.aed.deisiworldmeter;

import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<Pais> paisesT = new ArrayList<>();
    static ArrayList<Cidade> cidadesT = new ArrayList<>();
    static ArrayList<InputInvalido> inputsInvalidos = new ArrayList<>();
    static ArrayList<Populacao> populacaoT = new ArrayList<>();
    static ArrayList<String> codigosAlfa2Paises = new ArrayList<>();
    static HashMap<Integer, String> paisesHm = new HashMap<>();
    static HashMap<String, Pais> alfa2Pais = new HashMap<>();
    static HashMap<String, Pais> nomePorPais = new HashMap<>();
    static HashMap<Integer, String> idPorNomePais = new HashMap<>();
    static Set<String> codigosAlfa2Cidades = new HashSet<>();
    static int minId1 = 0;
    static int maxId1 = 0;

    public static boolean validaTipos(String[] linha) {
        if (linha.length == 4) {
            return linha[0].matches("\\d+") && linha[1].length() == 2 && linha[2].length() == 3;
        }
        if (linha.length == 5) {
            return linha[0].matches("\\d+") && linha[1].matches("\\d+") && linha[2].matches("\\d+")
                    && linha[3].matches("\\d+") && linha[4].matches("-?\\d+(\\.\\d+)?");
        }
        if (linha.length == 6) {
            return linha[3].matches("-?\\d+(\\.\\d+)?");
        }
        return false;
    }

    public static boolean validaExistePais(String[] paises) {
        for (Pais pais : paisesT) {
            if (pais.id == Integer.parseInt(paises[0])) {
                return true;
            }
        }
        return false;
    }

    public static boolean validaCidadeTemPais(String[] cidades) {
        for (int i = 0; i < paisesHm.size(); i++) {
            if (paisesHm.containsValue(cidades[0])) {
                return true;
            }
        }
        return false;
    }

    public static boolean validaPopulacaoTemPais(String[] populacao) {
        for (Pais pais : paisesT) {
            if (pais.id == Integer.parseInt(populacao[0])) {
                return true;
            }
        }
        return false;
    }


    public static String[] normalizaComando(String command) {
        String[] parts = command.split(" ");

        if (parts.length == 1) {
            return parts;
        } else if (parts[0].equals("COUNT_CITIES") || parts[0].equals("SUM_POPULATIONS")
                || parts[0].equals("GET_DUPLICATE_CITIES") ||
                parts[0].equals("GET_COUNTRIES_GENDER_GAP") || parts[0].equals("REMOVE_COUNTRY") ||
                parts[0].equals("GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES") || parts[0].equals("COUNT_REGIONS")) {
            for (int i = 0; i < parts.length; i++) {
                if (i > 1) {
                    parts[1] += " " + parts[i];
                    parts[i] = null;
                }
            }
            return parts;

        } else if (parts[0].equals("GET_CITIES_BY_COUNTRY") || parts[0].equals("GET_MISSING_HISTORY") ||
                parts[0].equals("GET_TOP_CITIES_BY_COUNTRY") || parts[0].equals("GET_TOP_POPULATION_INCREASE") ||
                parts[0].equals("GET_CITIES_AT_DISTANCE") || parts[0].equals("GET_CITIES_AT_DISTANCE2") ||
                parts[0].equals("GET_TOP_LAND_AREA") || parts[0].equals("GET_DENSITY_BELOW") ||
                parts[0].equals("GET_MOST_POPULOUS")) {

            for (int i = 0; i < parts.length; i++) {
                if (i > 2) {
                    parts[2] += " " + parts[i];
                    parts[i] = null;
                }
            }
            return parts;
        } else if (parts[0].equals("GET_HISTORY")) {
            for (int i = 0; i < parts.length; i++) {
                if (i > 3) {
                    parts[3] += " " + parts[i];
                    parts[i] = null;
                }
            }
            return parts;

        } else if (parts[0].equals("INSERT_CITY")) {
            for (int i = 0; i < parts.length; i++) {
                if (i > 2 && i < parts.length - 1 && i < parts.length - 2) {
                    parts[2] += " " + parts[i];
                    parts[i] = null;
                }
            }
            parts[3] = parts[parts.length - 2];
            parts[4] = parts[parts.length - 1];
            return parts;

        }
        return parts;
    }


    public static Result execute(String command) {

        String[] parts = normalizaComando(command);

        if (parts[0].equals("GET_MOST_POPULOUS")) {
            if (Character.isLetter(parts[2].charAt(0))) {
                return new Result(true, null, "Valor invalido");
            }
        }
        Result noCommandMatch = new Result(false, "Comando invalido", null);
        return switch (parts[0]) {
            case "COUNT_CITIES" -> Queries.countCities(parts[1]);
            case "COUNT_REGIONS" -> Queries.countRegions(parts[1]);
            case "GET_DENSITY_BELOW" -> Queries.getDensityBelow(parts[1], parts[2]);
            case "GET_CITIES_BY_COUNTRY" -> Queries.getCitiesByCountry(parts[1], parts[2]);
            case "SUM_POPULATIONS" -> Queries.sumPopulations(parts[1]);
            case "GET_HISTORY" -> Queries.getHistory(parts[1], parts[2], parts[3]);
            case "GET_MISSING_HISTORY" -> Queries.getMissingHistory(parts[1], parts[2]);
            case "GET_MOST_POPULOUS" -> Queries.getMostPopulous(parts[1], Integer.parseInt(parts[2]));
            case "GET_TOP_CITIES_BY_COUNTRY" -> Queries.getTopCitiesByCountry(parts[1], parts[2]);
            case "GET_DUPLICATE_CITIES" -> Queries.getDuplicateCities(parts[1]);
            case "GET_COUNTRIES_GENDER_GAP" -> Queries.getCountriesGenderGap(parts[1]);
            case "GET_TOP_POPULATION_INCREASE" -> Queries.getTopPopulationIncrease(parts[1], parts[2]);
            case "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES" -> Queries.getDuplicateCitiesDiferentCountries(parts[1]);
            case "GET_CITIES_AT_DISTANCE" -> Queries.getCitiesAtDistance(parts[1], parts[2]);
            case "GET_CITIES_AT_DISTANCE2" -> Queries.getCitiesAtDistance2(parts[1], parts[2]);
            case "GET_TOP_LAND_AREA" -> Queries.getTopLandArea(parts[1], parts[2]);
            case "INSERT_CITY" -> Queries.insertCity(parts[1], parts[2], parts[3], parts[4]);
            case "REMOVE_COUNTRY" -> Queries.removeCountry(parts[1]);
            case "HELP" -> Queries.help();
            case "quit" -> Queries.quit();
            default -> noCommandMatch;
        };
    }


    public static ArrayList getObjects(TipoEntidade tipo) {
        switch (tipo) {
            case PAIS:
                return new ArrayList<>(paisesT);

            case CIDADE:
                return new ArrayList<>(cidadesT);

            case INPUT_INVALIDO:
                ArrayList<String> inputsString = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    inputsString.add(inputsInvalidos.get(i).toString());
                }
                return inputsString;
            default:
                return null;
        }
    }

    static ArrayList getCountries(int minId, int maxId) {
        ArrayList lista = new ArrayList();
        
        for(Pais pais : paisesT){
            if(pais.id >= minId && pais.id <= maxId){
                lista.add(pais);
            }
        }
        return lista;
    }

    public static boolean parseFiles(File folder) {

        paisesT.clear();
        cidadesT.clear();
        inputsInvalidos.clear();
        populacaoT.clear();
        codigosAlfa2Paises.clear();
        codigosAlfa2Cidades.clear();
        paisesHm.clear();
        alfa2Pais.clear();
        idPorNomePais.clear();
        nomePorPais.clear();

        File ficheiroPaises = new File(folder, "paises.csv");
        File ficheiroCidades = new File(folder, "cidades.csv");
        File ficheiroPopulacao = new File(folder, "populacao.csv");

        try {

            int linhaErradaPaises = 0;
            int nrLinhaPaises = 2;
            int countValidasPaises = 0;
            int countInvalidasPaises = 0;
            Scanner leitorPaises = new Scanner(ficheiroPaises);
            leitorPaises.nextLine();
            while (leitorPaises.hasNextLine()) {
                String linha = leitorPaises.nextLine();
                String[] paises = linha.split(",");
                if (paises.length != 4 || !validaTipos(paises) || validaExistePais(paises)) {
                    if (countInvalidasPaises < 1) {
                        linhaErradaPaises = nrLinhaPaises;
                    }
                    nrLinhaPaises++;
                    countInvalidasPaises++;
                    if (leitorPaises.hasNextLine()) {
                        continue;
                    } else {
                        break;
                    }
                }
                int id = Integer.parseInt(paises[0].trim());
                String alfa2 = paises[1].trim();
                String alfa3 = paises[2].trim();
                String nome = paises[3];

                Pais country = new Pais(id, alfa2, alfa3, nome);
                paisesT.add(country);
                codigosAlfa2Paises.add(alfa2);
                alfa2Pais.put(alfa2, country);
                nomePorPais.put(nome, country);
                paisesHm.put(id, alfa2);
                idPorNomePais.put(id, nome);
                nrLinhaPaises++;
                countValidasPaises++;
            }


            int linhaErradaCidades = 0;
            int nrLinhaCidades = 2;
            int countValidasCidades = 0;
            int countInvalidasCidades = 0;
            Scanner leitorCidades = new Scanner(ficheiroCidades);
            leitorCidades.nextLine();


            while (leitorCidades.hasNextLine()) {
                String linha = leitorCidades.nextLine();
                String[] cidades = linha.split(",");
                if (cidades.length != 6 || !validaTipos(cidades) || !validaCidadeTemPais(cidades)) {
                    if (countInvalidasCidades < 1) {
                        linhaErradaCidades = nrLinhaCidades;
                    }
                    nrLinhaCidades++;
                    countInvalidasCidades++;
                    if (leitorCidades.hasNextLine()) {
                        continue;
                    } else {
                        break;
                    }
                }
                String alfa2 = cidades[0].trim();
                String cidade = cidades[1];
                String idRegiao = cidades[2].trim();
                double populacao = Double.parseDouble(cidades[3].trim());
                double latitude = Double.parseDouble(cidades[4].trim());
                double longitude = Double.parseDouble(cidades[5].trim());

                Cidade city = new Cidade(alfa2, cidade, idRegiao, populacao, latitude, longitude);
                cidadesT.add(city);
                codigosAlfa2Cidades.add(alfa2);
                nrLinhaCidades++;
                countValidasCidades++;
            }
            /*
            Iterator<Pais> iteradorPais = paisesT.iterator();
            int nrLinhaPaises2 = 1;
            int countInvalidasPaises2 = 0;
            while(iteradorPais.hasNext()){
                Pais pais = iteradorPais.next();
                nrLinhaPaises2++;
                if (!codigosAlfa2Cidades.contains(pais.alfa2)) {
                    if (countInvalidasPaises2 < 1) {
                        linhaErradaPaises = nrLinhaPaises2;
                    }
                    countValidasPaises--;
                    countInvalidasPaises++;
                    countInvalidasPaises2++;
                    iteradorPais.remove();
                    paisesHm.remove(pais.id);
                    alfa2Pais.remove(pais.alfa2);
                    idPorNomePais.remove(pais.id);
                }
            }
*/
            infoLinhas("paises.csv", countValidasPaises, countInvalidasPaises, linhaErradaPaises);
            infoLinhas("cidades.csv", countValidasCidades, countInvalidasCidades, linhaErradaCidades);


            int linhaErradaPopulacao = 0;
            int nrLinhaPopulacao = 2;
            int countValidasPopulacao = 0;
            int countInvalidasPopulacao = 0;
            Scanner leitorPopulacao = new Scanner(ficheiroPopulacao);
            leitorPopulacao.nextLine();

            while (leitorPopulacao.hasNextLine()) {
                String linha = leitorPopulacao.nextLine();
                String[] populacao = linha.split(",");
                if (populacao.length != 5 || !validaTipos(populacao) || !validaPopulacaoTemPais(populacao)) {
                    if (countInvalidasPopulacao < 1) {
                        linhaErradaPopulacao = nrLinhaPopulacao;
                    }
                    nrLinhaPopulacao++;
                    countInvalidasPopulacao++;
                    if (leitorPopulacao.hasNextLine()) {
                        continue;
                    } else {
                        break;
                    }
                }
                int id = Integer.parseInt(populacao[0].trim());
                int ano = Integer.parseInt(populacao[1].trim());
                int populacaoM = Integer.parseInt(populacao[2].trim());
                int populacaoF = Integer.parseInt(populacao[3].trim());
                Double densidade = Double.parseDouble(populacao[4].trim());

                Populacao population = new Populacao(id, ano, populacaoM, populacaoF, densidade);
                populacaoT.add(population);
                nrLinhaPopulacao++;
                countValidasPopulacao++;
            }
            infoLinhas("populacao.csv", countValidasPopulacao, countInvalidasPopulacao, linhaErradaPopulacao);


        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void infoLinhas(String nameFile, int linhasValidas, int linhasInvalidas, int primeiraErrada) {
        InputInvalido inputInvalido;
        if (linhasInvalidas == 0) {
            inputInvalido = new InputInvalido(nameFile, linhasValidas, linhasInvalidas, -1);
        } else {
            inputInvalido = new InputInvalido(nameFile, linhasValidas, linhasInvalidas, primeiraErrada);
        }
        inputsInvalidos.add(inputInvalido);

    }

    public static void main(String[] args) {
        System.out.println(getCountries(1, 1000));



    }
}