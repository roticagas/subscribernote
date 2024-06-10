package com.entertainment.subscriber.note.service;

import com.entertainment.subscriber.note.dao.SubscriberDao;
import com.entertainment.subscriber.note.model.ConfigModel;
import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.util.SubscriberConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SubscriberNoteService {
    private final Logger logger = LoggerFactory.getLogger(SubscriberNoteService.class);

    final ConfigCrudService configCrudService;

    final SubscriberCrudService subscriberCrudService;

    public SubscriberNoteService(ConfigCrudService configCrudService, SubscriberCrudService subscriberCrudService) {
        this.configCrudService = configCrudService;
        this.subscriberCrudService = subscriberCrudService;
    }


    public Mono<SubscriberModel> update0(SubscriberDao subscriberDao) {
        SubscriberModel request = SubscriberConverter.convertToSubscriberModel(subscriberDao);
        String description = "%s:-".formatted(request.getTitle());

        ConfigModel config = configCrudService.findByConfigKey(request.getTitle() + ".price").block();
        if (config != null) {
            description = "%s:%s".formatted(request.getTitle(), config.getConfigValue());
        }

        SubscriberModel existed = subscriberCrudService.findByNameAndTitle(request).block();
        if (existed == null) {
            request.setDescription(description);
            request.setModifiedAt(LocalDateTime.now());
            return subscriberCrudService.save(request);
        } else {
            existed.setDescription(description);
            existed.setModifiedAt(LocalDateTime.now());
            return subscriberCrudService.update(existed.getId(), existed);
        }
    }

    public Mono<SubscriberModel> update(SubscriberDao subscriberDao) {
        SubscriberModel request = SubscriberConverter.convertToSubscriberModel(subscriberDao);
        logger.debug("request: {}", request);
        return configCrudService.findByConfigKey(request.getTitle() + ".price")
                .flatMap(config -> updateOrCreateSubscriber(config, request)
                );
    }

    private Mono<SubscriberModel> updateOrCreateSubscriber(ConfigModel config, SubscriberModel request) {
        String description = (config != null) ?
                String.format("%s:%s", request.getTitle(), config.getConfigValue()) :
                String.format("%s:-", request.getTitle());
        request.setDescription(description);

        return subscriberCrudService.findByNameAndTitle(request)
                .flatMap(existingSubscriber -> {
                    existingSubscriber.setDescription(description);
                    existingSubscriber.setStatus(request.getStatus());
                    existingSubscriber.setModifiedAt(LocalDateTime.now());
                    return subscriberCrudService.update(existingSubscriber.getId(), existingSubscriber);
                })
                .switchIfEmpty(Mono.defer(() -> subscriberCrudService.save(request)));
    }
}