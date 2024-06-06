package com.entertainment.subscriber.note.service;

import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SubscriberService {
    private final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public Flux<SubscriberModel> findAll() {
        logger.debug("findAll");
        return subscriberRepository.findAll();
    }

    public Flux<SubscriberModel> findAllByName(String name) {
        logger.debug("findAllByName %s".formatted(name));
        return subscriberRepository.findAllByName(name);
    }

    public Flux<SubscriberModel> findAllByTitle(String title) {
        logger.debug("findAllByTitle %s".formatted(title));
        return subscriberRepository.findAllByTitle(title);
    }

    public Mono<SubscriberModel> save(SubscriberModel model) {

        SubscriberModel entity = new SubscriberModel(model.getName(), model.getTitle(), model.getDescription(), model.getStatus(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        logger.debug("save %s".formatted(entity));
        return subscriberRepository.save(entity);
    }

    public Mono<SubscriberModel> update(long id, SubscriberModel model) {
        return subscriberRepository.findById(id)
                .flatMap(existingModel -> {
                    existingModel.setName(model.getName());
                    existingModel.setTitle(model.getTitle());
                    existingModel.setDescription(model.getDescription());
                    existingModel.setStatus(model.getStatus());
                    existingModel.setRecurringAt(model.getRecurringAt());
                    existingModel.setModifiedAt(LocalDateTime.now());
                    return subscriberRepository.save(existingModel);
                });
    }
}
