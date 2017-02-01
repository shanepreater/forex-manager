package com.ovcs.forex.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovcs.forex.Trade;
import com.ovcs.forex.convertor.TradeHtmlConvertor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test that the file based repo works.
 */
public class FileBasedTradeRepositoryTest {

    FileBasedTradeRepository underTest;

    FileBasedTradeRepository.Configuration configuration;

    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        Path directory = Files.createTempDirectory("tradeTester");
        configuration = new FileBasedTradeRepository.Configuration();
        configuration.setFileStoreDirectory(directory.toAbsolutePath().toString());

        underTest = new FileBasedTradeRepository(configuration, objectMapper);
    }

    @After
    public void tearDown() throws Exception {
        Files.walkFileTree(configuration.getFileStoreDirectoryPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void testNoTradeFound() throws Exception {
        Trade trade = underTest.findByIdentifier("1234");
        assertNull(trade);
    }

    @Test
    public void testSavingTrade() throws Exception {
        Trade trade = new TradeHtmlConvertor().convert("12330719\t2017.02.01 10:06:09\tbuy\t0.20\tcadjpymicro\t86.863\t86.314\t87.486\t \t86.717\t0.00\t0.00\t0.00\t-0.21").iterator().next();

        underTest.save(trade);

        List<Trade> trades = underTest.findAll();
        assertNotNull(trades);
        assertEquals(1, trades.size());
        Trade savedTrade = trades.get(0);
        assertEquals(trade.getIdentifier(), savedTrade.getIdentifier());
    }
}