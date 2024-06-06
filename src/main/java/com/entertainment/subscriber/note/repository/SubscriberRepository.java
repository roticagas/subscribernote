package com.entertainment.subscriber.note.repository;

import com.entertainment.subscriber.note.model.SubscriberModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubscriberRepository extends R2dbcRepository<SubscriberModel, Long> {

    Flux<SubscriberModel> findAllByName(String name);

    Flux<SubscriberModel> findAllByTitle(String title);
}