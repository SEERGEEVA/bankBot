package com.SEERGEEVA.bankBot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Engine {

    private static final String cbrUrlFormat =
            "https://www.cbr.ru/currency_base/daily/?UniDbQuery.Posted=True&UniDbQuery.To=%s"; // date

    private static final String bankirosUrlFormat =
            "https://%s.bankiros.ru/currency/%s"; // city, currency

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    private KeyWordsUtil keys;

    public Engine(KeyWordsUtil keys) {

        this.keys = keys;
    }

    public EngineResponse processRequest(String message) throws IOException {

        EngineResponse engineResponse = new EngineResponse(null, false);

        RequestInfo info = UserRequestParser.parseFirst(message, keys);
        if (info == null) {
            logger.warn("Bot received non-query message");
            engineResponse.message = "There are no query in your message";
            return engineResponse;
        }

        if (info.pageType.equals(RequestInfo.PageType.BANKIROS)) {
            String finalUrl = String.format(bankirosUrlFormat, info.city, info.currency);

            logger.info("Establishing connection to: " + finalUrl);
            Document doc = Jsoup.connect(finalUrl).get();
            logger.info("Established connection successfully");

            logger.info("Parsing the page");
            String response = HtmlParser.parseBankiros(doc);
            logger.info("Parsed the page successfully");

            engineResponse.message = String.format("%s exchange rate in %s:\n",
                    info.currency.toUpperCase(), info.city.toUpperCase()) + response;
            engineResponse.isSuccess = true;
            return engineResponse;

        } else if (info.pageType.equals(RequestInfo.PageType.CBR)) {
            String finalUrl = String.format(cbrUrlFormat, info.date);

            logger.info("Establishing connection to: " + finalUrl);
            Document doc = Jsoup.connect(finalUrl).get();
            logger.info("Established connection successfully");

            logger.info("Parsing the page");
            String response = HtmlParser.parseCbr(doc, info.currency);
            logger.info("Parsed the page successfilly");

            engineResponse.message = String.format("%s exchange rate at %s:\n",
                    info.currency, info.date) + response;
            engineResponse.isSuccess = true;
            return engineResponse;
        }

        logger.warn("Unknown error while processing the request");
        engineResponse.message = "Something went wrong";
        return engineResponse;
    }
}
