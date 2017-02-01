package com.ovcs.forex;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public double getNetProfit() {
        if(grossProfit >= 0) {
            return grossProfit - swap;
        }
        return grossProfit;
    }

    @JsonIgnore
    public boolean isWinner() {
        return grossProfit > 0;
    }

    @JsonIgnore
    public boolean isClosed() {
        return closed != null;
    }

    @JsonIgnore
    public double getRiskPips() {
        if(initialStopLoss > openingPrice) {
            return (initialStopLoss - openingPrice) * pair.getPipMultiplier();
        }
        return (openingPrice - initialStopLoss) * pair.getPipMultiplier();
    }

    @JsonIgnore
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
