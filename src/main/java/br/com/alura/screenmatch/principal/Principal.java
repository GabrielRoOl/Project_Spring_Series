package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2def0c3f";


    public void exibeMenuI(){

        System.out.print("Digite o nome da série para busca: ");

        var nomeSerie = sc.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = converte.obterDados(json, DadosSerie.class);
        System.out.println(dados + "\n");

        List<DadosTemporada> temporada = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = converte.obterDados(json, DadosTemporada.class);
            temporada.add(dadosTemporada);
        }
        temporada.forEach(System.out::println);
        System.out.println();

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodios> episodiosTemporada = temporada.get(i).episodios();
//
//            System.out.println("Temporada " + (i + 1));
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            } System.out.println();
//        }

        temporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<String> nomes = Arrays.asList("Gabriel", "Daniel", "Alamir", "Adriane", "Taynara");

//        nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<DadosEpisodios> dadosEpisodios = temporada.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 10 epsodios.");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limit " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Map " + e))
//                .forEach(System.out::println);

//        System.out.println();

        List<Episodio> episodios = temporada.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);


//        System.out.print("Digite um trecho do titulo do epsódio: ");
//        var trechoTitulo = sc.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episódio não encontrado.");
//        }

//        System.out.println();
//
//        System.out.print("\nApartir de qual ano deseja ver os episódios? ");
//        var ano = sc.nextInt();
//        sc.nextLine();
//
//        System.out.println();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null &&  e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
       System.out.println(avaliacoesPorTemporada);

       DoubleSummaryStatistics est = episodios.stream()
               .filter(e -> e.getAvaliacao() > 0.0)
               .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage() + "\n" +
                "Melhor episódio: " + est.getMax() + "\n" +
                "Pior episódioo: " + est.getMin() + "\n" +
                "Quantidade: " + est.getCount());
    }


}
