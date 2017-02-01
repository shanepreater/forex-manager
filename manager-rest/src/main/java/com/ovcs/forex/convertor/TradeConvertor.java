package com.ovcs.forex.convertor;

import com.ovcs.forex.Trade;

import java.util.Collection;

/**
 * Converts Strings to trades.
 */
public interface TradeConvertor {
    Collection<Trade> convert(final String raw);
}
