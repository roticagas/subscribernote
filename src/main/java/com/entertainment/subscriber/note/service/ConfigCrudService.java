package com.entertainment.subscriber.note.service;

import com.entertainment.subscriber.note.model.ConfigModel;
import com.entertainment.subscriber.note.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ConfigCrudService {
    Logger logger = LoggerFactory.getLogger(ConfigCrudService.class);

    final ConfigRepository configRepository;

    public ConfigCrudService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public Flux<ConfigModel> findAll() {
        logger.debug("findAll");
        return configRepository.findAll();
    }

    @CacheEvict(value = "config", key = "#configModel.configKey")
    public Mono<ConfigModel> save(ConfigModel configModel) {
        ConfigModel entity = new ConfigModel(configModel.getConfigKey(), configModel.getConfigValue(), LocalDateTime.now(), LocalDateTime.now());
        logger.debug("save %s".formatted(entity));
        return configRepository.save(entity);
    }

    @CacheEvict(value = "config", key = "#configModel.configKey")
    public Mono<ConfigModel> update(ConfigModel configModel) {
        logger.debug("update %s".formatted(configModel));
        return configRepository.findByConfigKey(configModel.getConfigKey())
                .flatMap(
                        existingEntity -> {
                            existingEntity.setConfigValue(configModel.getConfigValue());
                            existingEntity.setModifiedAt(LocalDateTime.now());
                            return configRepository.save(existingEntity);
                        }
                ).switchIfEmpty(save(configModel));
    }

    @CacheEvict(value = "config", key = "#key")
    public Mono<Void> deleteByConfigKey(String key) {
        logger.debug("deleteByConfigKey %s".formatted(key));
        configRepository.deleteByConfigKey(key);
        return Mono.empty();
    }

    @Cacheable(value = "config", key = "#key")
    public Mono<ConfigModel> findByConfigKey(String key) {
        logger.debug("findByConfigKey %s".formatted(key));
        return configRepository.findByConfigKey(key);
    }
}
