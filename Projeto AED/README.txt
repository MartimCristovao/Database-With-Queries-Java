Vou explicar o funcionamento do comando criativo getTopLandArea:

O objetivo deste comando é obter um top dos países com maior área, em quilómetros quadrados.

O top é definido através do parâmetro recebido pela função num-results.

O que esta função faz é, para cada país, ir ao ano de 2024, e obter a população total, somando a população 
masculina com a população feminina, e em seguida dividir pela densidade populacional, este dado ainda não tinha sido usado em nenhuma 
outra função, por isso pareceu-nos bem usá-lo para a criativa.

Após ter o valor da área para o país, este valor irá ser normalizado pela função normaliza2CasasDecimais(), para arredondar à segunda
casa decimal.

Depois de ter todos os valores da área de cada país, é usado o Collections.sort para ordenar os países de forma descrescente quanto à sua 
área.

Por fim são obtidos os resultados consoante o numero definido no argumento, número de resultados, da função.

É de lembrar que esta função pode ainda obter os países com mais de xxx área em vez de ser baseado no número de resultados.

Achamos esta função bastante útil, pois a área de todos os países é um dado estatístico importante a ter.

O funcionamento desta função foi posteriormente comprovado em vídeo com recurso a uma base de dados pública, com os tamanhos de cada país.


Uso do ChatGPT:

Nós usámos o chatGPT para a função getDuplicateCitiesDifferentCountries, as interações foram as seguintes:

Pergunta: 
Num arraylist de objetos de cidades, nos quais objetos sao constituidos pelos parametros pais, nomeCidade, id,  
como encontrar cidades duplicadas mas so de paises diferentes, e adicionar ambas a um arraylist em java 

Resposta: 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Cidade {
    String pais;
    String nomeCidade;
    int id;

    public Cidade(String pais, String nomeCidade, int id) {
        this.pais = pais;
        this.nomeCidade = nomeCidade;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cidade{" +
                "pais='" + pais + '\'' +
                ", nomeCidade='" + nomeCidade + '\'' +
                ", id=" + id +
                '}';
    }
}

public class Main {
    public static void main(String[] args) {
        // Lista de cidades
        ArrayList<Cidade> cidades = new ArrayList<>();
        cidades.add(new Cidade("Brasil", "São Paulo", 1));
        cidades.add(new Cidade("EUA", "São Paulo", 2));
        cidades.add(new Cidade("Brasil", "Rio de Janeiro", 3));
        cidades.add(new Cidade("Portugal", "Porto", 4));
        cidades.add(new Cidade("Espanha", "Barcelona", 5));
        cidades.add(new Cidade("Argentina", "São Paulo", 6));
        cidades.add(new Cidade("Chile", "Santiago", 7));
        cidades.add(new Cidade("EUA", "Los Angeles", 8));

        // Mapa para rastrear nomes de cidades e seus respectivos países
        HashMap<String, HashSet<String>> cidadePaisMap = new HashMap<>();
        // Lista para armazenar as cidades duplicadas de países diferentes
        ArrayList<Cidade> cidadesDuplicadas = new ArrayList<>();

        // Preencher o mapa com as cidades
        for (Cidade cidade : cidades) {
            cidadePaisMap.putIfAbsent(cidade.nomeCidade, new HashSet<>());
            cidadePaisMap.get(cidade.nomeCidade).add(cidade.pais);
        }

        // Iterar novamente para encontrar duplicatas
        for (Cidade cidade : cidades) {
            HashSet<String> paises = cidadePaisMap.get(cidade.nomeCidade);
            if (paises.size() > 1) {
                cidadesDuplicadas.add(cidade);
            }
        }

        // Exibir as cidades duplicadas de países diferentes
        System.out.println("Cidades duplicadas de países diferentes:");
        for (Cidade cidade : cidadesDuplicadas) {
            System.out.println(cidade);
        }
    }
}


Deste código dado pelo chatGPT, nós claramente não o usámos todo, mas aplicámos alguns pontos do código à nossa função
por exemplo, a estrutura HashMap<String, HashSet<String>> ajudou-nos bastante, e o putIfAbsent, que adiciona ao hashMap algo, se ele
n tiver ja a key, ou se a key for null, se ja tiver ele retorna o valor da key passada. Isto deu imenso jeito, pois nesta função
tinhamos de usar todas as repetições de uma cidade, inclusivé a primeira encontrada, ao contrário do getDuplicateCities, em que 
não era necessário adicionar a primeira aparição da cidade repetida.