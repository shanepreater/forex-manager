package com.ovcs.forex;

/**
 * Currency
 */
public enum Currency {
    AUD("Australian dollar"),
    GBP("Pounds sterling"),
    USD("US Dollar"),
    CAD("Canadian Dollar"),
    EUR("Euro"),
    SEK("Swedish Krona"),
    CHF("Swiss Franc"),
    NZD("New zealand Dollar"),
    SGD("Singapore Dollar"),
    DKK("Danish Krone"),
    NOK("Norwegian Krone"),
    ZAR("South African Rand"),
    JPY("Japanese Yen"),
    MXN("Mexican Pesos"),
    HKD("Hong Kong Dollar"),
    HUF("Hungarian Forint"),
    PLN("N/A"),
    RUB("Rubol"),
    SILVER("Silver"),
    GOLD("Gold");


    private String humanReadable;

    private Currency(final String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public String getHumanReadable() {
        return humanReadable;
    }
}
