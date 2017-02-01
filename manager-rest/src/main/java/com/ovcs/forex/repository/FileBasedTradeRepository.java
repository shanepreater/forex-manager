package com.ovcs.forex.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ovcs.forex.Trade;
import com.ovcs.forex.TradeRepository;
import com.ovcs.forex.convertor.InstantConvertor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

/**
 * Repository which is backed with json files in a directory.
 */
@Service
public class FileBasedTradeRepository implements TradeRepository {

    private static final Logger log = LoggerFactory.getLogger(FileBasedTradeRepository.class);

    private Configuration configuration;

    private ObjectMapper objectMapper;

    private final Map<String, Trade> cache = new WeakHashMap<>();

    @Autowired
    public FileBasedTradeRepository(Configuration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;

        InstantConvertor instantConvertor = new InstantConvertor();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, instantConvertor.getDeserializer());
        module.addSerializer(Instant.class, instantConvertor.getSerializer());
        objectMapper.registerModule(module);
    }

    @Override
    public Trade findByIdentifier(String identifier) {
        if (Files.exists(configuration.getFileStoreDirectoryPath().resolve(identifier + ".json"))) {
            return getTrade(identifier);
        }
        return null;
    }

    @Override
    public void save(Trade trade) {
        cache.put(trade.getIdentifier(), trade);
        writeToDisk(trade);
    }

    @Override
    public List<Trade> findAll() {
        try {
            return Files.walk(configuration.getFileStoreDirectoryPath())
                    .filter(path -> path.endsWith(".json"))
                    .map(this::load)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Error finding the trades", e);
        }
    }

    @Override
    public List<Trade> findAllClosed() {
        try {
            return Files.walk(configuration.getFileStoreDirectoryPath())
                    .filter(path -> path.endsWith(".json"))
                    .map(this::load)
                    .filter(Trade::isClosed)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Error finding the trades", e);
        }
    }

    @Override
    public List<Trade> findAllClosedBetween(Instant from, Instant until) {
        try {
            return Files.walk(configuration.getFileStoreDirectoryPath())
                    .filter(path -> path.endsWith(".json"))
                    .map(this::load)
                    .filter(trade -> trade.isClosed() && trade.getClosed().isBefore(until) && trade.getClosed().isAfter(from))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Error finding the trades", e);
        }
    }

    private Trade getTrade(final String identifier) {
        if (cache.containsKey(identifier)) {
            log.debug("CACHE HIT: {}", identifier);
            return cache.get(identifier);
        }
        log.debug("CACHE MISS: {}" + identifier);
        Trade trade = loadFromDisk(identifier);
        cache.put(identifier, trade);
        return trade;
    }

    private Trade loadFromDisk(final String identifier) {
        Path jsonPath = configuration.getFileStoreDirectoryPath().resolve(identifier + ".json");
        return load(jsonPath);
    }

    private Trade load(Path jsonPath) {
        try (InputStream inputStream = Files.newInputStream(jsonPath)) {
            return objectMapper.readValue(inputStream, Trade.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read " + jsonPath + " from disk!", e);
        }
    }

    private void writeToDisk(final Trade trade) {
        Path jsonPath = configuration.getFileStoreDirectoryPath().resolve(trade.getIdentifier() + ".json");
        try (OutputStream outputStream = Files.newOutputStream(jsonPath)) {
            objectMapper.writeValue(outputStream, trade);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write " + trade.getIdentifier() + " to disk!", e);
        }
    }

    @Data
    public static final class Configuration {
        private String fileStoreDirectory;

        public Path getFileStoreDirectoryPath() {
            return Paths.get(fileStoreDirectory);
        }
    }
}
