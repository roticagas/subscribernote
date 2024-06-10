package com.entertainment.subscriber.note.controller;

import com.entertainment.subscriber.note.dao.SubscriberDao;
import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.service.SubscriberNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SubscriberNoteController {

    final SubscriberNoteService subscriberNoteService;

    public SubscriberNoteController(SubscriberNoteService subscriberNoteService) {
        this.subscriberNoteService = subscriberNoteService;
    }

    @PostMapping("/notes")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SubscriberModel> update(@RequestBody SubscriberDao subscriberDao) {
        return subscriberNoteService.update(subscriberDao);
    }

}
