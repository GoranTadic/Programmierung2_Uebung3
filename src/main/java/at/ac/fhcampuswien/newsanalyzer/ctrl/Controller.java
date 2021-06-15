package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import static java.util.Comparator.*;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "596a01f19bdc4831938a8de7647759d5";  //TODO add your api key

	public void process(NewsApi news) {
		//TODO implement Error handling

		//TODO load the news based on the parameters

		//TODO implement methods for analysis

		NewsResponse newsResponse = null;
		List<Article> articles = null;

		try{
			newsResponse = news.getNews();
			articles = newsResponse.getArticles();
		}catch(Exception e){
			System.out.println("Exception: " + e.getMessage());
			System.out.println("Keine News vorhanden");
		}

		if(newsResponse != null && !articles.isEmpty()){
			//articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()+ "\n"));
			System.out.println("----Geordnet----");
			List<Article> result = articles.stream().sorted((object1, object2) -> object1.getTitle().compareTo(object2.getTitle())).collect(Collectors.toList());
			result.stream().forEach(article -> System.out.println("\t"+article.toString()+ "\n"));

			System.out.println("Es wurden "+articles.size() + " Artikeln ausgegeben");

			String getProvider =
					articles.stream()
							.collect(Collectors.groupingBy(article -> article.getSource().getName(),Collectors.counting()))
							.entrySet()
							.stream()
							.max(Comparator.comparingInt(t->t.getValue().intValue())).get().getKey();
			System.out.println("Provider with the most articles: "+getProvider);
			try{
				String ShortestAuthor =
						articles.stream().filter(article -> Objects.nonNull(article.getAuthor()))
								.min(Comparator.comparingInt(article -> article.getAuthor().length()))
								.get().getAuthor();

				System.out.println("Shortest Author: "+ShortestAuthor);
			}catch(NullPointerException e){
				System.out.println("Error" + e.getMessage());
			}catch(Exception e){
				System.out.println("Error " + e.getMessage());
			}

			String save = "";
			Scanner scanner = new Scanner(System.in);

			do {
				System.out.println("\n\nDo you want to save the articles? [y/n]");
				save = scanner.nextLine();
				if(save.equalsIgnoreCase("y") || save.equalsIgnoreCase("n") ||
						save.equalsIgnoreCase("yes") || save.equalsIgnoreCase("no")) break;
			} while(true);


			if(save.equals("y")) {
				for(Article article : articles){
					try{
						URL uri = new URL(article.getUrl());
						BufferedReader te = new BufferedReader( new InputStreamReader(uri.openStream()));
						String inputLine;
						BufferedWriter writer = new BufferedWriter(new FileWriter(article.getTitle().substring(0,2)+".html"));
						while((inputLine = te.readLine()) != null){
							writer.write(inputLine);
							writer.newLine();
						}
						te.close();
						writer.close();
					}catch(Exception e){
						System.out.println("Fatal Error: " + e.getMessage());
					}
				}
			}
		}
	}


	public Object getData() {

		return null;
	}
}
