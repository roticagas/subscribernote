package com.entertainment.subscriber.note.controller;

import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.service.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class SubscriberController {

    final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SubscriberModel> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title
    ) {
        if (name != null && !name.isEmpty()) {
            return subscriberService.findAllByName(name);
        } else if (title != null && !title.isEmpty()) {
            return subscriberService.findAllByTitle(title);
        } else {
            return subscriberService.findAll();
        }
    }

    @PostMapping("/subscribers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SubscriberModel> save(@RequestBody SubscriberModel subscriberModel) {
        return subscriberService.save(subscriberModel);
    }

    @PutMapping("/subscribers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SubscriberModel> update(@PathVariable("id") long id, @RequestBody SubscriberModel subscriberModel) {
        return subscriberService.update(id, subscriberModel);
    }


}
