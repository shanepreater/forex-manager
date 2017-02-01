package com.ovcs.forex.convertor;

import com.ovcs.forex.CurrencyPair;
import com.ovcs.forex.Trade;
import com.ovcs.forex.TradeType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Convertor which takes the String as an HTMl fragment and returns the Trade details.
 * <p>
 * This uses the fragments exported from the report generater in MT4. It is a tabbed delimited set of the following fields:
 * <ul>
 * <li>Identifier</li>
 * <li>Open Time</li>
 * <li>Type</li>
 * <li>Lot size</li>
 * <li>Currency Pair</li>
 * <li>Opening Price</li>
 * <li>Stop Loss</li>
 * <li>Take Profit</li>
 * <li>Close Time</li>
 * <li>Closing Price</li>
 * <li>Commission</li>
 * <li>Taxes</li>
 * <li>Swap</li>
 * <li>Profit</li>
 * </ul></p>
 */
@Component
public class TradeHtmlConvertor implements TradeConvertor {

    private static final String FIELD_DELIMITER = "\t";

    private static final String LINE_DELIMITER = "\n";

    @Override
    public Collection<Trade> convert(String raw) {
        List<Trade> trades = new ArrayList<>();
        if (!StringUtils.isEmpty(raw)) {
            String[] individualTradeLines = raw.split(LINE_DELIMITER);
            for (String individualTrade : individualTradeLines) {
                Trade trade = convertTrade(individualTrade);
                if (trade != null) {
                    trades.add(trade);
                }
            }
        }
        return trades;
    }

    private Trade convertTrade(String individualTrade) {
        final String[] fields = individualTrade.split(FIELD_DELIMITER);
        if (fields.length != 14) {
            throw new IllegalArgumentException("Unexpected trade line: " + individualTrade);
        }
        Trade trade = new Trade();
        trade.setIdentifier(fields[0].trim());
        trade.setOpened(convertInstant(fields[1]));
        trade.setType(convertType(fields[2]));
        trade.setSize(convertDouble(fields[3]));
        trade.setPair(convertPair(fields[4]));
        trade.setOpeningPrice(convertDouble(fields[5]));
        trade.setStopLoss(convertDouble(fields[6]));
        trade.setTakeProfit(convertDouble(fields[7]));
        String closedTime = fields[8].trim();
        if (StringUtils.isEmpty(closedTime) == false) {
            //This trade has closed so take the profit details.
            trade.setClosed(convertInstant(closedTime));
            trade.setClosingPrice(convertDouble(fields[9]));
            trade.setSwap(convertDouble(fields[12]));
            trade.setGrossProfit(convertDouble(fields[13]));
        }
        return trade;
    }

    private CurrencyPair convertPair(String field) {
        field = field.trim();
        if (field.endsWith("micro")) {
            return convertPair(field.substring(0, field.length() - 5));
        }
        return CurrencyPair.valueOf(field.toUpperCase());
    }

    private double convertDouble(String field) {
        return Double.parseDouble(field.trim());
    }

    private TradeType convertType(String field) {
        if (field.trim().equalsIgnoreCase("buy")) {
            return TradeType.BUY;
        }
        return TradeType.SELL;
    }

    private Instant convertInstant(String field) {
        LocalDateTime parsed = DateTimeFormatter
                .ofPattern("yyyy.MM.dd HH:mm:ss")
                .parse(field.trim(), LocalDateTime::from);
        long epochSecond = parsed.toEpochSecond(ZoneOffset.UTC);
        return Instant.ofEpochMilli(epochSecond);
    }
}
