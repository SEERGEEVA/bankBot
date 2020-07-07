package com.SEERGEEVA.bankBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

    public static String parseCbr(Document doc, String currencyCode) {
        StringBuilder builder = new StringBuilder();
        //Document doc = Jsoup.parse(htmlData);

        int countTillValue = -1;
        Elements table = doc.getElementsByTag("td");
        for (Element e : table) {
            if (e.ownText().equals(currencyCode)) {
                countTillValue = 3;
            }
            if (countTillValue == 0) {
                builder.append(e.ownText());
            }
            --countTillValue;
        }

        return builder.toString();
    }

    public static String parseBankiros(Document doc) {
        StringBuilder builder = new StringBuilder();
        //Document doc = Jsoup.parse(htmlData);

        Element preTable = doc.getElementsByTag("tbody").get(1);
        Elements table = preTable.select("tr[class=productBank tr-turn tr-link  row body even" +
                        "],tr[class=productBank tr-turn tr-link  row body odd]");

        int counter = 0;
        for (Element e : table) {
            builder.append(e.child(0).text()).append('\n');
            builder.append("Buy at: ").append(e.child(1).text()).append('\n');
            builder.append("Sell at: ").append(e.child(2).text()).append('\n');
            builder.append('\n');

            if (++counter == 5) {
                break;
            }
        }

        return builder.toString();
    }
}
