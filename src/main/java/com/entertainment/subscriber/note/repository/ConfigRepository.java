package com.entertainment.subscriber.note.repository;

import com.entertainment.subscriber.note.model.ConfigModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConfigRepository extends R2dbcRepository<ConfigModel, Long> {
    Mono<ConfigModel> findByConfigKey(String key);

    void deleteByConfigKey(String key);
}
