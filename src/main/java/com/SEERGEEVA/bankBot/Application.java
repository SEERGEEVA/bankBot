package com.SEERGEEVA.bankBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;


public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {

        logger.info("Loading keywords info");
        ObjectMapper mapper = new ObjectMapper();
        KeyWordsUtil keys = mapper.readValue(
                Application.class.getClassLoader().getResourceAsStream("keyWords.json"),
                KeyWordsUtil.class);

        logger.info("Loaded keywords info successfully");

        logger.info("Establishing connection engine");
        Engine engine = new Engine(keys);
        logger.info("Established connection engine successfully");

        logger.info("Initializing telegram bot");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new LongPollBot(engine));
        } catch (TelegramApiRequestException e) {
            logger.warn(e.getMessage());
        }
        logger.info("Initialized telegram bot successfully");

    }
}
