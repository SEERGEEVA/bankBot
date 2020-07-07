package com.SEERGEEVA.bankBot;

import java.util.Objects;

public class RequestInfo {

    public String city;
    public String area;
    public String currency;
    public String date;
    public boolean isPopular;
    public PageType pageType;

    public RequestInfo(String city, String currency, PageType pageType) {
        this(city, null, currency, null, true, pageType);
    }

    public RequestInfo(String city, String currency, String date, PageType pageType) {
        this(city, null, currency, date, true, pageType);
    }

    public RequestInfo(String city, String area, String currency, String date,
                       boolean isPopular, PageType pageType) {
        this.city = city;
        this.area = area;
        this.currency = currency;
        this.date = date;
        this.isPopular = isPopular;
        this.pageType = pageType;
    }

    public enum PageType {
        BANKIROS,
        CBR
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestInfo)) return false;
        RequestInfo info = (RequestInfo) o;
        return isPopular == info.isPopular &&
                Objects.equals(city, info.city) &&
                Objects.equals(area, info.area) &&
                Objects.equals(currency, info.currency) &&
                Objects.equals(date, info.date) &&
                pageType == info.pageType;
    }
}
