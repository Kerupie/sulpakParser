package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static List<Smartphones> list = new ArrayList<>();
    public static void getLinks(String url) throws IOException {
        String[] linkList = new String[5000];
        String baseURL = "https://www.sulpak.kz";
        Document connection = Jsoup.connect(url).get();
        Elements document = connection.getElementsByClass("product__item-images flex__block");
        for (int i = 0; i < document.size(); i++) {
            linkList[i]=baseURL+document.get(i).attr("href");
//            System.out.println(linkList[i]+"#characteristicsTab");
            getElements(linkList[i]);
        }
        }
        public static void getElements(String currentLink) throws IOException {
            Map <String, String> map = new HashMap<>();
            Document document = Jsoup.connect(currentLink+"#characteristicsTab").get();
//            System.out.println(document.getElementsByClass("product__characteristics-item-title"));
            Elements strTitle = document.getElementsByClass("product__characteristics-item-title");
            Elements strValue = document.getElementsByClass("product__characteristics-item-val");
            String name = document.getElementsByClass("product__item-name").text();
            String price = document.getElementsByClass("product__item-price").text().replaceAll("\\D+", "");
            int priceInt = price.equals("") ? -1 : Integer.parseInt(price);
            Smartphones smartphone = new Smartphones();
            for (int i = 0; i < strTitle.size(); i++){
                map.put(strTitle.get(i).text(), strValue.get(i).text());
                smartphone.setName(name);
                smartphone.setPrice(priceInt);
            }
            smartphone.setCharacteristics(map);
            list.add(smartphone);
        }
    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String url = "https://www.sulpak.kz/f/smartfoniy/astana";
        for (int i=1; i<29; i++) {
            getLinks(url+"?page="+i);
            System.out.println(url+"?page="+i);
            System.out.println("Page "+i+" was parsed");
        }
        gson.toJson(list, new FileWriter("C:\\Users\\krump\\Desktop\\info1.json"));
        }
}
