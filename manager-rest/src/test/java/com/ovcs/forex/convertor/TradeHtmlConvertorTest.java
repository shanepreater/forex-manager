package com.ovcs.forex.convertor;

import com.ovcs.forex.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Convertor test.
 */
public class TradeHtmlConvertorTest {

    private TradeHtmlConvertor underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new TradeHtmlConvertor();
    }

    @Test
    public void testEmptyString() throws Exception {
        Collection<Trade> trades = underTest.convert("");
        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    public void testSingleTrade() throws Exception {
        final String rawFragment = "10372620\t2017.01.25 21:31:59\tsell\t0.10\tusdmxnmicro\t21.25006\t21.25000\t20.40313\t2017.02.01 10:01:03\t20.82989\t0.00\t0.00\t0.01\t1.61";
        Collection<Trade> trades = underTest.convert(rawFragment);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
        assertEquals(1, trades.size());
        Trade trade = trades.iterator().next();
        assertNotNull(trade);
        assertEquals("10372620", trade.getIdentifier());
        assertEquals(true, trade.isClosed());
        assertEquals(1.6, trade.getNetProfit(), 0.0000000000001);
    }

    @Test
    public void testMultipleTrades() throws Exception {
        final String rawFragment = "11166648\t2017.01.27 17:47:17\tbuy\t0.30\taudcadmicro\t0.99119\t0.98472\t1.00358\t \t0.99185\t0.00\t0.00\t0.00\t0.12\n" +
                "12330719\t2017.02.01 10:06:09\tbuy\t0.20\tcadjpymicro\t86.863\t86.314\t87.486\t \t86.717\t0.00\t0.00\t0.00\t-0.21\n" +
                "9525734\t2017.01.23 23:44:01\tbuy\t0.20\teuraudmicro\t1.41850\t1.40657\t1.43991\t \t1.42119\t0.00\t0.00\t-0.17\t0.33\n" +
                "12329932\t2017.02.01 10:04:00\tbuy\t0.20\tgbpsgdmicro\t1.77880\t1.76911\t1.79664\t \t1.78024\t0.00\t0.00\t0.00\t0.16";
        Collection<Trade> trades = underTest.convert(rawFragment);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
        assertEquals(4, trades.size());
    }

    @Test
    public void testReceivingCrap() throws Exception {
        final String fragment = "Some old bollox";
        try {
            underTest.convert(fragment);
            fail("Exception should be thrown");
        }catch(IllegalArgumentException e) {
            assertEquals("Unexpected trade line: Some old bollox", e.getMessage());
        }

    }
}