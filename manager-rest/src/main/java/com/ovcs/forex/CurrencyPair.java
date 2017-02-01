package com.ovcs.forex;

import static com.ovcs.forex.Currency.*;

/**
 * Currency Pairs.
 */
public enum CurrencyPair {
    EURSEK(EUR, SEK, 1000),
    AUDCAD(AUD, CAD, 10000),
    EURAUD(EUR, AUD, 1000),
    EURCHF(EUR, CHF, 1000),
    GBPNZD(GBP, NZD, 1000),
    GBPSGD(GBP, SGD, 10000),
    USDCAD(USD, CAD, 10000),
    CADJPY(CAD, JPY, 100),
    EURDKK(EUR, DKK, 1000),
    EURHKD(EUR, HKD, 1000),
    EURNOK(EUR, NOK, 1000),
    EURZAR(EUR, ZAR, 1000),
    GBPDKK(GBP, DKK, 1000),
    GBPSEK(GBP, SEK, 1000),
    USDDKK(USD, DKK, 1000),
    USDNOK(USD, NOK, 1000),
    USDMXN(USD, MXN, 1000),
    USDSEK(USD, SEK, 1000),
    AUDCHF(AUD, CHF, 1000),
    AUDJPY(AUD,JPY, 100),
    AUDNZD(AUD, NZD, 1000),
    AUDUSD(AUD, USD, 1000),
    CADCHF(CAD, CHF, 1000),
    EURCAD(EUR, CAD, 10000),
    EURGBP(EUR, GBP, 1000),
    EURHUF(EUR, HUF, 0.1),
    EURJPY(EUR, JPY, 100),
    EURNZD(EUR, NZD, 1000),
    EURPLN(EUR, PLN, 1000),
    EURSGC(EUR, SGD, 10000),
    EURUSD(EUR, USD, 1000),
    GBPAUD(GBP, AUD, 1000),
    GBPCAD(GBP, CAD, 10000),
    GBPCHF(GBP,CHF, 1000),
    GBPJPY(GBP, JPY, 100),
    GBPUSD(GBP, USD, 1000),
    USDCHF(USD, CHF, 1000),
    USDHUF(USD, HUF, 0.1),
    USDJPY(USD, JPY, 100),
    USDPLN(USD, PLN, 1000),
    USDRUB(USD, RUB, 1000),
    USDSGD(USD, SGD, 10000),
    GBPNOK(GBP, NOK, 1000),
    GOLD(Currency.GOLD, null, 0.01),
    SILVER(Currency.SILVER, null, 0.1);

    private final Currency lhs;
    private final Currency rhs;
    private final double pipMultiplier;

    private CurrencyPair(Currency lhs, Currency rhs, double pipMultiplier) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.pipMultiplier = pipMultiplier;
    }

    public Currency getLhs() {
        return lhs;
    }

    public Currency getRhs() {
        return rhs;
    }

    public double getPipMultiplier() {
        return pipMultiplier;
    }
}
