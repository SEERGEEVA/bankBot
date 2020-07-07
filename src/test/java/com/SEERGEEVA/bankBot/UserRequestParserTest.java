package com.SEERGEEVA.bankBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UserRequestParserTest {

    private static KeyWordsUtil util;

    private static RequestInfo cityAndCurrency =
            new RequestInfo("moskva", "usd", RequestInfo.PageType.BANKIROS);

    private static RequestInfo dateAndCurrency =
            new RequestInfo(null, "USD", "01.01.2020", RequestInfo.PageType.CBR);

    @BeforeClass
    public static void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            util = mapper.readValue(
                    UserRequestParserTest.class.getClassLoader().getResourceAsStream("keyWords.json"),
                    KeyWordsUtil.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseFirstTest() {
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("москва доллары", util));
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("Москва доллары", util));
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("мсква доллары", util));
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("доллары в москве", util));
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("москва долары", util));
        assertEquals(cityAndCurrency, UserRequestParser.parseFirst("москва долларов", util));

        assertEquals(dateAndCurrency, UserRequestParser.parseFirst("доллары 01.01.2020", util));
        assertEquals(dateAndCurrency, UserRequestParser.parseFirst("долларов 01.01.2020", util));
        assertEquals(dateAndCurrency, UserRequestParser.parseFirst("доллары в москве 01.01.2020", util));

        assertNull(UserRequestParser.parseFirst("доллары 01.2020", util));
        assertNull(UserRequestParser.parseFirst("доллары", util));
        assertNull(UserRequestParser.parseFirst("01.01.2020", util));
        assertNull(UserRequestParser.parseFirst("москва", util));
    }
}
