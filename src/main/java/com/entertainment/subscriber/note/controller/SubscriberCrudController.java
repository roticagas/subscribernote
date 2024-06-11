package com.entertainment.subscriber.note.controller;

import com.entertainment.subscriber.note.model.SubscriberModel;
import com.entertainment.subscriber.note.service.SubscriberCrudService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SubscriberCrudController {

    final SubscriberCrudService subscriberCrudService;

    public SubscriberCrudController(SubscriberCrudService subscriberCrudService) {
        this.subscriberCrudService = subscriberCrudService;
    }

    @GetMapping("/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SubscriberModel> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") Integer pageNo, // start at 0
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        if (name != null && !name.isEmpty()) {
            return subscriberCrudService.findAllByName(name, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
        } else if (title != null && !title.isEmpty()) {
            return subscriberCrudService.findAllByTitle(title, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
        } else {
            return subscriberCrudService.findAllBy(PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
        }
    }

    @PostMapping("/subscribers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SubscriberModel> save(@RequestBody SubscriberModel subscriberModel) {
        return subscriberCrudService.save(subscriberModel);
    }

    @PutMapping("/subscribers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SubscriberModel> update(@PathVariable("id") long id, @RequestBody SubscriberModel subscriberModel) {
        return subscriberCrudService.update(id, subscriberModel);
    }


}
