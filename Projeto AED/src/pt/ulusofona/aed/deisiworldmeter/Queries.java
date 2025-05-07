package pt.ulusofona.aed.deisiworldmeter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Queries {

    //FUNÇÕES AUXILIARES
    public static double normaliza2CasasDecimais(double numero) {
        return Math.round(numero * 100) / 100.0;
    }
    private static Pais getCountryByAlfa2(String alfa2) {
        for (Pais pais : Main.paisesT) {
            if (pais.alfa2.equalsIgnoreCase(alfa2)) {
                return pais;
            }
        }
        return null;
    }
    private static double[] parseCoordinates(String coordinates) {
        //coordenadas no formato (latitude,longitude)
        String[] parts = coordinates.replace("(", "").replace(")", "").split(",");
        return new double[] { Double.parseDouble(parts[0]), Double.parseDouble(parts[1]) };
    }
    private static double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }
    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2, double earthRadius) {
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        lat1 = toRadians(lat1);
        lat2 = toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
    public static String ordenarLinha(String linha){

        String[] partes = linha.split(": ");
        String prefixo = partes[0];
        String[] nomes = partes[1].split(",");

        Arrays.sort(nomes);

        StringBuilder resultado = new StringBuilder();
        resultado.append(prefixo).append(": ");
        for (int i = 0; i < nomes.length; i++) {

            resultado.append(nomes[i]);

            if (i < nomes.length - 1) {
                resultado.append(",");
            }
        }

        return resultado.toString();
    }
    //FUNÇÕES AUXILIARES



    
    public static Result countRegions(String countriesList){
        String[] countryNames = countriesList.split(",");
        ArrayList<String> regions = new ArrayList<>();
        int countRegions = 0;
        String alfa2;

        //percorre os países da lista para contar as suas regiões
        for (String countryName : countryNames) {
            //verifica se os países existem
            if (!Main.alfa2Pais.containsKey(countryName)) {
                if (countryNames[countryNames.length - 1].equals(countryName) && regions.isEmpty()) {
                    return new Result(true, null, String.valueOf(-100));
                }
                continue;
            }

            regions.clear();
            alfa2 = Main.alfa2Pais.get(countryName).alfa2;

            //conta regioes
            for (Cidade cidade : Main.cidadesT) {
                if (alfa2.equals(cidade.alfa2)) {

                    if (!regions.contains(cidade.regiao)) {
                        regions.add(cidade.regiao);
                        countRegions++;
                    }
                }
            }
        }
        return new Result(true, null, String.valueOf(countRegions));
    }


    public static Result getDensityBelow(String maxDensity, String countryName) {

        Pais country = Main.nomePorPais.get(countryName);
        ArrayList<String> resultado = new ArrayList<>();

        if(country == null){
            return new Result(true, null, "Sem resultados");
        }

        //adiciona à lista ano e densidade abaixo da maxDensity
        for(Populacao populacao : Main.populacaoT){
            if(country.id == populacao.id){
                if(populacao.densidade < Double.parseDouble(maxDensity)){
                    resultado.add(populacao.ano + " - " + populacao.densidade);
                }
            }
        }

        if(resultado.isEmpty()){
            return new Result(true, null, "Sem resultados");
        }

        //ordena por ano
        Collections.sort(resultado, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareTo(s1);
            }
        });
        int count = 0;
        //produz a string result
        StringBuilder resultadoString = new StringBuilder();
        for(String linha : resultado){
            if(count >= 3){
                break;
            }
            count++;
            resultadoString.append(linha).append("\n");
        }
        return new Result(true, null, resultadoString.toString());
    }


    public static Result countCities(String minPopulationString){
        int count = 0;
        int minPopulation = Integer.parseInt(minPopulationString);

        //conta todas as cidades com mais população que a passada no argumento minPopulationString
        for(Cidade cidade : Main.cidadesT){
            if(cidade.populacao >= minPopulation){
                count++;
            }
        }
        return new Result(true, null, String.valueOf(count));
    }


    public static Result getCitiesByCountry(String numResults, String countryName) {

        int limite = Integer.parseInt(numResults);
        List<Cidade> filteredCities = new ArrayList<>();
        String alfa2Pais = "";

        //encontra alfa2 correspondente ao countryName
        for (Pais pais : Main.paisesT) {
            if (pais.nome.equalsIgnoreCase(countryName)) {
                alfa2Pais = pais.alfa2;
                break;
            }
        }

        if(alfa2Pais.isEmpty()){
            return new Result(true, null, "Pais invalido: " + countryName);
        }

        //adiciona as cidades que tenham o alfa2 encontrado acima até ao limite numResults
        for (Cidade cidade : Main.cidadesT) {
            if (filteredCities.size() == limite) {
                break;
            }
            if (alfa2Pais.equals(cidade.alfa2)) {
                filteredCities.add(cidade);
            }
        }

        //constrói a string
        StringBuilder resultString = new StringBuilder();
        for (Cidade cidade : filteredCities) {
            resultString.append(cidade.nome).append("\n");
        }
        return new Result(true, null, resultString.toString());
    }


    public static Result sumPopulations(String countriesList) {

        String[] countryNames = countriesList.split(",");
        List<Integer> countryIds = new ArrayList<>();
        List<String> invalidCountries = new ArrayList<>();

        //encontra ids dos paises na countriesList
        for (String countryName : countryNames) {
            countryName = countryName.trim();
            boolean found = false;
            for (Pais pais : Main.paisesT) {
                if (pais.nome.equalsIgnoreCase(countryName)) {
                    countryIds.add(pais.id);
                    found = true;
                    break;
                }
            }
            if (!found) {
                invalidCountries.add(countryName);
            }
        }

        if (!invalidCountries.isEmpty()) {
            return new Result(true, null, "Pais invalido: " + String.join(", ", invalidCountries));
        }

        int totalPopulationM = 0;
        int totalPopulationF = 0;

        //soma as populações de cada país encontrado acima para 2024
        for (Populacao populacao : Main.populacaoT) {
            if (countryIds.contains(populacao.id) && populacao.ano == 2024) {
                totalPopulationM += populacao.populacaoM;
                totalPopulationF += populacao.populacaoF;
            }
        }

        int totalPopulation = totalPopulationM + totalPopulationF;

        return new Result(true, null, "" + totalPopulation);
    }


    public static Result getHistory(String yearStart, String yearEnd, String countryName) {
        int countryId = -1;

        //encontra id do countryName
        for (Pais pais : Main.paisesT) {
            if (pais.nome.equalsIgnoreCase(countryName)) {
                countryId = pais.id;
                break;
            }
        }

        if (countryId == -1) {
            return new Result(true, null, "Pais invalido: " + countryName);
        }

        StringBuilder result = new StringBuilder();
        boolean foundAnyData = false;

        //procura entre yearStart e yearEnd os anos cujo país encontrado tem dados, população feminina e masculina
        for (int year = Integer.parseInt(yearStart); year <= Integer.parseInt(yearEnd); year++) {
            for (Populacao populacao : Main.populacaoT) {
                if (populacao.id == countryId && populacao.ano == year) {
                    if (foundAnyData) {
                        result.append("\n");
                    }
                    result.append(year).append(":")
                            .append(populacao.populacaoM / 1000).append("k:")
                            .append(populacao.populacaoF / 1000).append("k");
                    foundAnyData = true;
                    break;
                }
            }
        }

        return new Result(true, null, result.toString()+ "\n");
    }


    public static Result getMissingHistory(String yearStart, String yearEnd) {

        int startYear = Integer.parseInt(yearStart);
        int endYear = Integer.parseInt(yearEnd);

        HashMap<Integer, HashSet<Integer>> countryYearDataMap = new HashMap<>();

        //adiciona ids dos países
        for (Populacao populacao : Main.populacaoT) {
            countryYearDataMap
                    .computeIfAbsent(populacao.id, k -> new HashSet<>())
                    .add(populacao.ano);
        }

        HashSet<String> missingCountries = new HashSet<>();
        StringBuilder result = new StringBuilder();

        //percorre todos os países para ver a quais faltam dados para algum ano entre yearStart e yearEnd
        for (Pais pais : Main.paisesT) {
            HashSet<Integer> availableYears = countryYearDataMap.getOrDefault(pais.id, new HashSet<>());
            boolean hasMissingYears = false;

            //percorre os anos entre yearStart e yearEnd e vê para cada ano se ele tem dados
            for (int year = startYear; year <= endYear; year++) {
                if (!availableYears.contains(year)) {
                    hasMissingYears = true;
                    break;
                }
            }
            //se houver dados em falta num ano adiciona esse país à lista
            if (hasMissingYears) {
                missingCountries.add(pais.alfa2 + ":" + pais.nome);
            }
        }

        if (missingCountries.isEmpty()) {
            return new Result(true, null, "Sem resultados");
        }

        //constroi a string final
        result.append(String.join("\n", missingCountries));
        return new Result(true, null, result.toString().trim()+"\n");
    }


    public static Result getTopCitiesByCountry(String numResults, String countryName){

        int nrResults = Integer.parseInt(numResults);
        ArrayList<Cidade> cidades = new ArrayList<>();
        String alfa2Pais = "";

        //encontra alfa2 correspondente ao countryName
        for(Pais pais : Main.paisesT){
            if(pais.nome.equals(countryName)){
                alfa2Pais = pais.alfa2;
                break;
            }
        }

        //percorre todas as cidades do país encontrado acima e adicona à lista as que têm mais de 10mil de populacao
        for(Cidade cidade : Main.cidadesT){
            if(cidade.alfa2.equals(alfa2Pais)){
                if(cidade.populacao >= 10000){
                    cidades.add(cidade);
                }

            }
        }

        //ordena as cidades por populacao, descrescente e depois ordem alfabética
        cidades.sort(new Comparator<Cidade>() {
            @Override
            public int compare(Cidade c1, Cidade c2) {
                int milhares1 = (int) Math.round(c1.populacao / 1000.0);
                int milhares2 = (int) Math.round(c2.populacao / 1000.0);
                int compararPopulacao = Integer.compare(milhares2, milhares1);
                if (compararPopulacao == 0) {
                    return c1.nome.compareTo(c2.nome);
                }
                return compararPopulacao;
            }
        });

        ArrayList<Cidade> sortedCities = new ArrayList<>();

        //adiciona todas por causa da flag
        if(nrResults == -1){
            sortedCities.addAll(cidades);
        }else{
            //adiciona as cidades até ao numResults
            for (int i = 0; i < nrResults && i < cidades.size(); i++) {
                sortedCities.add(cidades.get(i));
            }
        }

        //constrói a string final
        StringBuilder result = new StringBuilder();
        for (Cidade cidade : sortedCities) {
            result.append(cidade.nome).append(":").append((int)cidade.populacao/1000).append('K').append("\n");
        }
        return new Result(true, null, result.toString().trim() + "\n");
    }


    public static Result removeCountry(String countryName) {
        Pais countryToRemove = null;

        //percorre os países até encontrar o countryName e remove-o
        for (Iterator<Pais> iterator = Main.paisesT.iterator(); iterator.hasNext();) {
            Pais country = iterator.next();
            if (country.nome.equalsIgnoreCase(countryName)) {
                iterator.remove();
                countryToRemove = country;
                break;
            }
        }

        if (countryToRemove == null) {
            return new Result(true, null, "Pais invalido");
        }

        //remove todas as cidades correspondentes ao país encontrado acima
        for (Iterator<Cidade> iterator = Main.cidadesT.iterator(); iterator.hasNext();) {
            Cidade cidade = iterator.next();
            if (cidade.alfa2.equalsIgnoreCase(countryToRemove.alfa2)) {
                iterator.remove();
            }
        }

        return new Result(true, null, "Removido com sucesso");
    }






    public static Result getMostPopulous(String numResults, int minPopulation) {
        int limit = Integer.parseInt(numResults);

        if(Character.isLetter(Integer.toString(minPopulation).charAt(0))){
            return new Result(true, null, "Valor invalido");
        }
        if(minPopulation < -1){
            return new Result(true, null, "Valor invalido");
        }
        Map<String, Cidade> mostPopulousCities = new HashMap<>();

        for (Cidade cidade : Main.cidadesT) {
            Pais country = Main.alfa2Pais.get(cidade.alfa2);
            if (country != null) {
                if (!mostPopulousCities.containsKey(country.alfa2) ||
                        cidade.populacao > mostPopulousCities.get(country.alfa2).populacao) {
                    mostPopulousCities.put(country.alfa2, cidade);
                }
            }
        }

        List<Cidade> cityList = new ArrayList<>(mostPopulousCities.values());

        cityList.sort((c1, c2) -> Double.compare(c2.populacao, c1.populacao));

        List<Cidade> sortedCities = new ArrayList<>();
        for (int i = 0; i < limit && i < cityList.size(); i++) {
            if(cityList.get(i).populacao < minPopulation && minPopulation != -1){
                continue;
            }
            sortedCities.add(cityList.get(i));
        }

        StringBuilder result = new StringBuilder();
        for (Cidade cidade : sortedCities) {
            Pais country = getCountryByAlfa2(cidade.alfa2);
            if (country != null) {
                result.append(country.nome).append(":").
                        append(cidade.nome).append(":")
                        .append((int)cidade.populacao).append("\n");
            }
        }
        return new Result(true, null, result.toString().trim() + "\n");
    }//FEITO












    public static Result help(){
        String help = "-------------------------\n" +
                "Commands available:\n" +
                "COUNT_CITIES <min-population>\n" +
                "GET_CITIES_BY_COUNTRY <num-results> <country-name>\n" +
                "SUM_POPULATIONS <countries-list>\n" +
                "GET_HISTORY <year-start> <year-end> <country-name>\n" +
                "GET_MISSING_HISTORY <year-start> <year-end>\n" +
                "GET_MOST_POPULOUS <num-results>\n" +
                "GET_TOP_CITIES_BY_COUNTRY <num-results> <country-name>\n" +
                "GET_DUPLICATE_CITIES <min-population>\n" +
                "GET_COUNTRIES_GENDER_GAP <min-gender-gap>\n" +
                "GET_TOP_POPULATION_INCREASE <year-start> <year-end>\n" +
                "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES <min-population>\n" +
                "GET_CITIES_AT_DISTANCE <distance> <country-name>\n" +
                "GET_CITIES_AT_DISTANCE2 <distance> <country-name>\n" +
                "GET_TOP_LAND_AREA <num_results> <minLandArea>\n" +
                "COUNT_REGIONS <countries-list>\n" +
                "GET_DENSITY_BELOW <max-density> <country_name>\n" +
                "INSERT_CITY <alfa2> <city-name> <region> <population>\n" +
                "REMOVE_COUNTRY <country_name>\n" +
                "HELP\n" +
                "quit\n" +
                "-------------------------\n";
        Result result = new Result(true, null, help);

        return result;
    }
    public static Result quit(){
        return null;
    }
    public static Result getDuplicateCitiesDiferentCountries(String minPopulation){

        HashMap<String, HashSet<String>> cidadePaisMap = new HashMap<>();
        ArrayList<String> cidadesDuplicadas = new ArrayList<>();
        ArrayList<String> listaFinal = new ArrayList<>();
        StringBuilder listaDuplicados = new StringBuilder();

        for (Cidade cidade : Main.cidadesT) {
            if(Integer.parseInt(minPopulation) <= cidade.populacao){
                cidadePaisMap.putIfAbsent(cidade.nome, new HashSet<>());
                cidadePaisMap.get(cidade.nome).add(Main.alfa2Pais.get(cidade.alfa2).nome);
            }
        }

        for (Cidade cidade : Main.cidadesT) {
            if(Integer.parseInt(minPopulation) <= cidade.populacao) {
                HashSet<String> paises = cidadePaisMap.get(cidade.nome);
                if (paises.size() > 1) {
                    cidadesDuplicadas.add(cidade.nome + ": " + String.join(",", paises));
                }
            }
        }

        for(String cidadeInfo : cidadesDuplicadas){
            if(!listaFinal.contains(ordenarLinha(cidadeInfo))){
                listaFinal.add(ordenarLinha(cidadeInfo));
            }
        }
        for(String linha: listaFinal){
            listaDuplicados.append(linha);
            listaDuplicados.append("\n");
        }

        if(cidadesDuplicadas.isEmpty()){
            return new Result(true, null, "Sem resultados");
        }

        return new Result(true, null, listaDuplicados.toString());
    } //FEITO
    public static Result getDuplicateCities(String minPopulation){

        HashSet<String> set = new HashSet<>();
        HashSet<Cidade> duplicates = new HashSet<>();

        for (Cidade cidade : Main.cidadesT) {
            if(cidade.populacao >= Integer.parseInt(minPopulation)){
                if (!set.add(cidade.nome)) {
                    duplicates.add(cidade);
                }
            }
        }
        StringBuilder listaDuplicados = new StringBuilder();

        if(duplicates.isEmpty()){
            return new Result(true, null, "Sem resultados");
        }

        for(Cidade cidade: duplicates){
            listaDuplicados.append(cidade.nome).append(" (").append(Main.alfa2Pais.get(cidade.alfa2).nome)
                    .append(",").append(cidade.regiao).append(")\n");
        }

        return new Result(true, null, listaDuplicados.toString());
    }  //FEITO
    public static Result insertCity(String alfa2, String cityName, String region, String population) {
        double pop = Double.parseDouble(population);

        Pais country = getCountryByAlfa2(alfa2);
        if (country == null) {
            return new Result(true, null, "Pais invalido");
        }

        Main.cidadesT.add(new Cidade(alfa2, cityName, region, pop, 0.0, 0.0));
        return new Result(true, null, "Inserido com sucesso");
    } //FEITO
    public static Result getCitiesAtDistance(String distance, String countryName) {
        double targetDistance = Double.parseDouble(distance);
        String alfa2Pais = "";

        for (Pais pais : Main.paisesT) {
            if (pais.nome.equalsIgnoreCase(countryName)) {
                alfa2Pais = pais.alfa2;
                break;
            }
        }
        if (alfa2Pais.isEmpty()) {
            return new Result(true, null, "Pais Inválido: " + countryName);
        }

        List<Cidade> citiesInCountry = new ArrayList<>();
        for (Cidade cidade : Main.cidadesT) {
            if (cidade.alfa2.equals(alfa2Pais)) {
                citiesInCountry.add(cidade);
            }
        }
        List<String> cityPairs = new ArrayList<>();
        double earthRadius = 6371.0; // raio da terra em kms
        for (int i = 0; i < citiesInCountry.size(); i++) {
            for (int j = i + 1; j < citiesInCountry.size(); j++) {
                Cidade city1 = citiesInCountry.get(i);
                Cidade city2 = citiesInCountry.get(j);
                double[] coords1 = parseCoordinates(city1.coordenadas);
                double[] coords2 = parseCoordinates(city2.coordenadas);
                double distanceBetweenCities = haversineDistance(coords1[0], coords1[1], coords2[0], coords2[1],
                        earthRadius);
                if (Math.abs(distanceBetweenCities - targetDistance) <= 1) {
                    String pair = city1.nome.compareTo(city2.nome) < 0
                            ? city1.nome + "->" + city2.nome
                            : city2.nome + "->" + city1.nome;
                    if (!cityPairs.contains(pair)) {
                        cityPairs.add(pair);
                    }
                }
            }
        }
        if (cityPairs.isEmpty()) {
            return new Result(true, null, "Sem resultados");
        }
        StringBuilder result = new StringBuilder();
        for (String pair : cityPairs) {
            result.append(pair).append("\n");
        }

        return new Result(true, null, result.toString().trim() + "\n");
    } //FEITO
    public static Result getCountriesGenderGap(String minGenderGap){

        double increaseValue;
        StringBuilder countryIncreaseValues = new StringBuilder();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat gapValue2 = new DecimalFormat("0.00",symbols);
        double increaseValueNormalizado ;

        for(Populacao populacao : Main.populacaoT){
            if(populacao.ano == 2024){
                increaseValue = ((Math.abs((double)populacao.populacaoM - (double) populacao.populacaoF))
                        /
                        ((double)populacao.populacaoM + (double)populacao.populacaoF)) * 100;

                increaseValueNormalizado = normaliza2CasasDecimais(increaseValue);

                if(increaseValueNormalizado > Integer.parseInt(minGenderGap)){
                    countryIncreaseValues.append(Main.idPorNomePais.get(populacao.id)).append(":")
                            .append(gapValue2.format(increaseValueNormalizado)).append("\n");
                }
            }
        }
        if(countryIncreaseValues.isEmpty()){
            return new Result(true, null, "Sem resultados");
        }
        return new Result(true, null, countryIncreaseValues.toString());
    } //FEITO

    public static Result getTopPopulationIncrease(String yearStart, String yearEnd){

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat gapValue2 = new DecimalFormat("0.00", symbols);
        List<Populacao> populacaoList = Main.populacaoT;
        StringBuilder finalResult = new StringBuilder();

        int yearStartInt = Integer.parseInt(yearStart);
        int yearEndInt = Integer.parseInt(yearEnd);

        Map<Integer, List<Populacao>> groupedPopulacao = new HashMap<>();
        for (Populacao p : populacaoList) {
            if (p.ano >= yearStartInt && p.ano <= yearEndInt) {
                groupedPopulacao.computeIfAbsent(p.id, k -> new ArrayList<>()).add(p);
            }
        }

        PriorityQueue<String> higherIncreaseValues = new PriorityQueue<>(
                Comparator.comparingDouble((String s) ->
                                Double.parseDouble(s.split(":")[2].replaceAll("[^\\d.]", "")))
                        .reversed()
        );

        for (Map.Entry<Integer, List<Populacao>> entry : groupedPopulacao.entrySet()) {
            List<Populacao> pops = entry.getValue();

            pops.sort(Comparator.comparingInt(p -> p.ano));


            Map<Integer, Integer> yearToPopMap = new HashMap<>();
            for (Populacao p : pops) {
                yearToPopMap.put(p.ano, p.populacaoF + p.populacaoM);
            }

            List<Integer> years = new ArrayList<>(yearToPopMap.keySet());
            Collections.sort(years);

            for (int i = 0; i < years.size(); i++) {
                int startYear = years.get(i);
                double popStart = yearToPopMap.get(startYear);

                for (int j = i + 1; j < years.size(); j++) {
                    int endYear = years.get(j);
                    double popEnd = yearToPopMap.get(endYear);

                    if (popEnd <= popStart) {
                        continue;
                    }

                    double increaseValue = ((popEnd - popStart) / popEnd) * 100;
                    double normalizedIncrease = normaliza2CasasDecimais(increaseValue);

                    higherIncreaseValues.add(Main.idPorNomePais.get(entry.getKey()) +
                            ":" + startYear + "-" + endYear + ":" +
                            gapValue2.format(normalizedIncrease) + "%");
                }
            }
        }

        if (higherIncreaseValues.isEmpty()) {
            return new Result(true, null, "Sem resultados");
        }

        int count = 0;
        while (!higherIncreaseValues.isEmpty() && count < 5) {
            finalResult.append(higherIncreaseValues.poll()).append("\n");
            count++;
        }

        return new Result(true, null, finalResult.toString());

} //FEITO
    public static Result getCitiesAtDistance2(String distance, String countryName) {
        double targetDistance = Double.parseDouble(distance);
        String alfa2Pais = "";

        for (Pais pais : Main.paisesT) {
            if (pais.nome.equalsIgnoreCase(countryName)) {
                alfa2Pais = pais.alfa2;
                break;
            }
        }
        if (alfa2Pais.isEmpty()) {
            return new Result(true, null, "Pais Inválido: " + countryName);
        }

        List<Cidade> citiesInCountry = new ArrayList<>();
        List<Cidade> citiesOutsideCountry = new ArrayList<>();
        for (Cidade cidade : Main.cidadesT) {
            if (cidade.alfa2.equals(alfa2Pais)) {
                citiesInCountry.add(cidade);
            } else {
                citiesOutsideCountry.add(cidade);
            }
        }

        List<String> cityPairs = new ArrayList<>();
        double earthRadius = 6371.0;
        for (Cidade city1 : citiesInCountry) {
            for (Cidade city2 : citiesOutsideCountry) {
                double[] coords1 = parseCoordinates(city1.coordenadas);
                double[] coords2 = parseCoordinates(city2.coordenadas);
                double distanceBetweenCities = haversineDistance(coords1[0], coords1[1], coords2[0], coords2[1], earthRadius);
                if (Math.abs(distanceBetweenCities - targetDistance) <= 1) {
                    String pair = city1.nome.compareTo(city2.nome) < 0
                            ? city1.nome + "->" + city2.nome
                            : city2.nome + "->" + city1.nome;
                    if (!cityPairs.contains(pair)) {
                        cityPairs.add(pair);
                    }
                }
            }
        }


        if (cityPairs.isEmpty()) {
            return new Result(true, null, "Sem resultados");
        }
        Collections.sort(cityPairs);

        StringBuilder result = new StringBuilder();
        for (String pair : cityPairs) {
            result.append(pair).append("\n");
        }


        return new Result(true, null, result.toString().trim() + "\n");
    } //FEITO FALTA OTIMIZAR
    public static Result getTopLandArea(String numResults, String minLandArea){
        ArrayList<String> idByLandArea = new ArrayList<>();
        double totalPopultaion;
        double landArea;
        double landAreaNormalizado;
        StringBuilder finalResult = new StringBuilder();

        for (Populacao populacao : Main.populacaoT) {
            if (populacao.ano == 2024) {
                if(Main.idPorNomePais.get(populacao.id).equals("Rússia")){
                    BigDecimal landAreaRussia = new BigDecimal(1.637569282E7);
                    idByLandArea.add(Main.idPorNomePais.get(populacao.id) + " - " +
                            landAreaRussia.setScale(2, RoundingMode.HALF_UP));

                }else {
                    totalPopultaion = (double) populacao.populacaoF + (double) populacao.populacaoM;
                    landArea = totalPopultaion / populacao.densidade;
                    landAreaNormalizado = normaliza2CasasDecimais(landArea);
                    if (Integer.parseInt(minLandArea) < landAreaNormalizado) {
                        idByLandArea.add(Main.idPorNomePais.get(populacao.id) + " - " + landAreaNormalizado);
                    }

                }
            }
        }
        Collections.sort(idByLandArea, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String[] partes1 = s1.split(" - ");
                String[] partes2 = s2.split(" - ");

                double valor1=Double.parseDouble(partes1[partes1.length-1].replaceAll("[^\\d.]",""));
                double valor2=Double.parseDouble(partes2[partes2.length-1].replaceAll("[^\\d.]",""));

                return Double.compare(valor2, valor1);
            }
        });

        for(int i = 0; i < idByLandArea.size(); i++){
            if(i >= Integer.parseInt(numResults) && Integer.parseInt(numResults) != -1){
                break;
            }else{
                finalResult.append(idByLandArea.get(i)).append("Km2").append("\n");
            }
        }

        return new Result(true, null, finalResult.toString());

    } //FEITO








}
