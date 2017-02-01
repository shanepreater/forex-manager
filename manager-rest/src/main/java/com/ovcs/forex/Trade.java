package com.ovcs.forex;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * Represents a trade on the Foreign exchange.
 */
@Data
@EqualsAndHashCode(of = {"identifier"})
public class Trade {
    private String identifier;
    private Instant opened;
    private double openingPrice;
    private double size;
    private TradeType type;
    private CurrencyPair pair;
    private double initialStopLoss;
    private double stopLoss;
    private double takeProfit;
    private Instant closed;
    private double closingPrice;
    private int pipChange;
    private double grossProfit;
    private double swap;

    public double getNetProfit() {
        if(grossProfit >= 0) {
            return grossProfit - swap;
        }
        return grossProfit;
    }

    public boolean isWinner() {
        return grossProfit > 0;
    }

    public boolean isClosed() {
        return closed != null;
    }

    public double getRiskPips() {
        if(initialStopLoss > openingPrice) {
            return (initialStopLoss - openingPrice) * pair.getPipMultiplier();
        }
        return (openingPrice - initialStopLoss) * pair.getPipMultiplier();
    }

    public double getRewardPips() {
        if(closingPrice > openingPrice) {
            return (closingPrice - openingPrice) * pair.getPipMultiplier();
        }
        return (openingPrice - closingPrice) * pair.getPipMultiplier();
    }

    public void copyFrom(Trade otherTrade) {
        setStopLoss(otherTrade.getStopLoss());
        setTakeProfit(otherTrade.getTakeProfit());
        setClosed(otherTrade.getClosed());
        setClosingPrice(otherTrade.getClosingPrice());
        setSwap(otherTrade.getSwap());
        setGrossProfit(otherTrade.getGrossProfit());
    }
}
