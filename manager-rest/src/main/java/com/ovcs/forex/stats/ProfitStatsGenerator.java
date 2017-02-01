package com.ovcs.forex.stats;

import com.ovcs.forex.Trade;
import com.ovcs.forex.TradeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring backed stat calculator
 */
@Component
public class ProfitStatsGenerator {

    private TradeRepository repository;

    @Autowired
    public ProfitStatsGenerator(final TradeRepository repository) {
        this.repository = repository;
    }

    RiskRewardStats generate() {
        List<PipSummary> winners = new ArrayList<>();
        List<PipSummary> losers = new ArrayList<>();

        List<Trade> trades = repository.findAll();
        double avgRisk = 0.0;
        double avgReward = 0.0;
        double profit = 0.0;
        for (Trade trade : trades) {
            if (trade.isClosed()) {
                PipSummary summary = new PipSummary();
                summary.setRisk(trade.getRiskPips());
                summary.setReward(trade.getRewardPips());
                summary.setProfit(trade.getNetProfit());
                avgRisk += summary.getRisk();
                profit += summary.getProfit();
                if (trade.isWinner()) {
                    //Add to the winner details.
                    winners.add(summary);
                    avgReward += summary.getReward();
                } else {
                    losers.add(summary);
                }
            }
        }
        avgRisk = avgRisk / trades.size();
        avgReward = avgReward / winners.size();
        double rewardR = avgReward / avgRisk;

        return new RiskRewardStats(avgRisk, avgReward, winners.size() / trades.size(), rewardR, profit);
    }

    @Data
    private static class PipSummary {
        private double risk;
        private double reward;
        private double profit;
    }

}
