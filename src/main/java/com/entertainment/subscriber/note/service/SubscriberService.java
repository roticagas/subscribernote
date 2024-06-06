package com.entertainment.subscriber.note.service;

import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SubscriberService {

    final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public Flux<SubscriberModel> findAll() {
        return subscriberRepository.findAll();
    }

    public Flux<SubscriberModel> findAllByName(String name) {
        return subscriberRepository.findAllByName(name);
    }

    public Flux<SubscriberModel> findAllByTitle(String title) {
        return subscriberRepository.findAllByTitle(title);
    }

    public Mono<SubscriberModel> save(SubscriberModel model) {
        return subscriberRepository.save(model);
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
