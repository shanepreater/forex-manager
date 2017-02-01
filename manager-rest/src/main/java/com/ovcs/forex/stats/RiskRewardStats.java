package com.ovcs.forex.stats;

import lombok.Data;

/**
 * Overall stats of the results of closed trades.
 */
@Data
public class RiskRewardStats {
    private final double riskPips;
    private final double rewardPips;
    private final double conversion;
    private final double rewardR;
    private final double totalProfit;
}
