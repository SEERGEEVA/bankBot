package com.SEERGEEVA.bankBot;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserRequestParser {

    public static RequestInfo parseFirst(String message, KeyWordsUtil keys) {
        String[] words = message.toLowerCase().split(" "); //TODO:: punctuation!!

        String city = null, currency = null, date = null;
        for (String word : words) {
            if (date == null && isDate(word)) {
                date = word;
            }
            if (date == null && city == null) {
                String thisCity = checkCities(word, keys);
                if (thisCity != null) {
                    city = thisCity;
                    continue;
                }
            }
            if (currency == null) {
                String thisCurrency = checkCurrencies(word, keys);
                if (thisCurrency != null) {
                    currency = thisCurrency;
                }
            }
            if (currency != null && (date != null || city != null)) {
                break;
            }
        }

        if (!(currency != null && (date != null || city != null))) {
            return null;
        }

        if (date != null) {
            return new RequestInfo(null, currency.toUpperCase(), date, RequestInfo.PageType.CBR);
        } else {
            return new RequestInfo(city, currency, RequestInfo.PageType.BANKIROS);
        }
    }

    protected static String checkCities(String word, KeyWordsUtil keys) {
        for (String city : keys.cities.keySet()) {
            int distance = calculateDistance(word, city);
            if (city.charAt(0) != word.charAt(0)) {
                continue;
            }
            if (city.length() < 3 && distance == 0) {
                return keys.cities.get(city);
            } else if (city.length() >= 4 && city.length() < 7 && distance <= 2) {
                return keys.cities.get(city);
            } else if (city.length() >= 7 && distance <= 3) {
                return keys.cities.get(city);
            }
        }

        return null;
    }

    protected static String checkCurrencies(String word, KeyWordsUtil keys) {
        for (String currency : keys.currencies.keySet()) {
            int distance = calculateDistance(word, currency);
            if (distance <= 2) {
                return keys.currencies.get(currency);
            }
        }

        return null;
    }

    protected static int calculateDistance(CharSequence source, CharSequence target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Parameter must not be null");
        }
        int sourceLength = source.length();
        int targetLength = target.length();
        if (sourceLength == 0) return targetLength;
        if (targetLength == 0) return sourceLength;
        int[][] dist = new int[sourceLength + 1][targetLength + 1];
        for (int i = 0; i < sourceLength + 1; i++) {
            dist[i][0] = i;
        }
        for (int j = 0; j < targetLength + 1; j++) {
            dist[0][j] = j;
        }
        for (int i = 1; i < sourceLength + 1; i++) {
            for (int j = 1; j < targetLength + 1; j++) {
                int cost = source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1;
                dist[i][j] = Math.min(Math.min(dist[i - 1][j] + 1, dist[i][j - 1] + 1), dist[i - 1][j - 1] + cost);
                if (i > 1 &&
                        j > 1 &&
                        source.charAt(i - 1) == target.charAt(j - 2) &&
                        source.charAt(i - 2) == target.charAt(j - 1)) {
                    dist[i][j] = Math.min(dist[i][j], dist[i - 2][j - 2] + cost);
                }
            }
        }
        return dist[sourceLength][targetLength];
    }

    protected static boolean isDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
