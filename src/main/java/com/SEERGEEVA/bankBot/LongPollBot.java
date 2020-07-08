package com.SEERGEEVA.bankBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class LongPollBot extends TelegramLongPollingBot {

    private Engine engine;
    private final Logger logger = LoggerFactory.getLogger(Application.class);

    public LongPollBot(Engine engine) {
        super();
        this.engine = engine;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String inMessage = update.getMessage().getText();

        logger.info("Bot received the message: " + inMessage);

        logger.info("Processing the request");
        EngineResponse outMessage;
        try {
             outMessage = engine.processRequest(inMessage);
        } catch (IOException e) {
            logger.error("Can't process the message", e);
            return;
        }
        logger.info("Processed the request successfully");

        if (!outMessage.isSuccess && update.getMessage().getChat().isGroupChat()) {
            logger.warn("Message wasn't sent because of non-query message from group chat");
            return;
        }

        SendMessage finalMessage = new SendMessage();
        finalMessage.enableMarkdown(true);
        finalMessage.setChatId(update.getMessage().getChatId().toString());

        if (outMessage.message.isEmpty() || !outMessage.isSuccess) {
            logger.warn("Engine returns empty message. Message will be changed");
            outMessage.message = "Can't process your request";
        }

        finalMessage.setText(outMessage.message);

        try {
            this.execute(finalMessage);
            logger.info("Sent message successfully");
        } catch (TelegramApiException e) {
            logger.error("Can't send a message", e);
        }

    }

    @Override
    public String getBotUsername() {
        return "itmo_seergeeva_bank_bot";
    }

    @Override
    public String getBotToken() {
        return "1331132371:AAETSjGld5iEKlMpaKWgi9gsBiJujDSsSxM";
    }
}
