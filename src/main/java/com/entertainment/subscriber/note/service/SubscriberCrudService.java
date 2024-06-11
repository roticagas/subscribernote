package com.entertainment.subscriber.note.service;

import com.entertainment.subscriber.note.util.TrackTime;
import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SubscriberCrudService {
    private final Logger logger = LoggerFactory.getLogger(SubscriberCrudService.class);

    final SubscriberRepository subscriberRepository;

    public SubscriberCrudService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @TrackTime
    public Flux<SubscriberModel> findAllBy(PageRequest pageRequest) {
        logger.debug("findAll %s".formatted(pageRequest));
        return subscriberRepository.findAllBy(pageRequest);
    }

    @TrackTime
    public Flux<SubscriberModel> findAllByName(String name, PageRequest pageRequest) {
        logger.debug("findAllByName %s %s".formatted(name, pageRequest));
        return subscriberRepository.findAllByName(name, pageRequest);
    }

    @TrackTime
    public Flux<SubscriberModel> findAllByTitle(String title, PageRequest pageRequest) {
        logger.debug("findAllByTitle %s %s".formatted(title, pageRequest));
        return subscriberRepository.findAllByTitle(title, pageRequest);
    }

    @TrackTime
    public Mono<SubscriberModel> save(SubscriberModel model) {
        SubscriberModel entity = new SubscriberModel(model.getName(), model.getTitle(), model.getDescription(), model.getStatus(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        logger.debug("save %s".formatted(entity));
        return subscriberRepository.save(entity);
    }

    @TrackTime
    public Mono<SubscriberModel> update(long id, SubscriberModel model) {
        logger.debug("update %d, %s".formatted(id, model));
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

    @TrackTime
    public Mono<SubscriberModel> findByNameAndTitle(SubscriberModel model) {
        return subscriberRepository.findByNameAndTitle(model.getName(), model.getTitle());
    }
}
