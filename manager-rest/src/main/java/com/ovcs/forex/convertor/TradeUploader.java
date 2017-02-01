package com.ovcs.forex.convertor;

import com.ovcs.forex.Trade;
import com.ovcs.forex.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Uploader which knows how to save the trade and merge any updates as they appear.
 */
@Service
public class TradeUploader {

    private final TradeRepository repository;

    private final TradeHtmlConvertor htmlConvertor;

    @Autowired
    public TradeUploader(final TradeRepository repository, TradeHtmlConvertor htmlConvertor) {
        this.repository = repository;
        this.htmlConvertor = htmlConvertor;
    }

    public String uploadTrade(final Trade newTrade) {
        if(StringUtils.isEmpty(newTrade.getIdentifier())) {
            throw new IllegalArgumentException("Trade does not have a ticket number!");
        }
        Trade existingTrade = repository.findByIdentifier(newTrade.getIdentifier());
        if(existingTrade == null) {
            //This is the first time we have seen this trade.
            newTrade.setInitialStopLoss(newTrade.getStopLoss());
            repository.save(newTrade);
        } else {
            existingTrade.copyFrom(newTrade);
            repository.save(existingTrade);
        }
        return newTrade.getIdentifier();
    }

    public void uploadTrades(Collection<Trade> trades) {
        for(Trade trade: trades) {
            uploadTrade(trade);
        }
    }

    public void uploadTrades(Trade ... trades) {
        if(trades != null) {
            for(Trade trade : trades) {
                uploadTrade(trade);
            }
        }
    }

    public void uploadFromFragment(final String rawFragment) {
        uploadTrades(htmlConvertor.convert(rawFragment));
    }
}
