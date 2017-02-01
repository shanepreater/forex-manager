package com.ovcs.forex;

import java.time.Instant;
import java.util.List;

/**
 * Main repo interface for holding and finding trades.
 */
public interface TradeRepository {
    Trade findByIdentifier(final String identifier);

    void save(Trade trade);

    List<Trade> findAll();

    List<Trade> findAllClosed();

    List<Trade> findAllClosedBetween(final Instant from, final Instant until);
}
