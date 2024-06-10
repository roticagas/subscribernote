package com.entertainment.subscriber.note.controller;

import com.entertainment.subscriber.note.model.ConfigModel;
import com.entertainment.subscriber.note.service.ConfigCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ConfigCrudController {

    final ConfigCrudService configCrudService;

    public ConfigCrudController(ConfigCrudService configCrudService) {
        this.configCrudService = configCrudService;
    }

    @GetMapping("/configs")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigModel> findAll() {
        return configCrudService.findAll();
    }

    @PostMapping("/configs")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConfigModel> save(@RequestBody ConfigModel configModel) {
        return configCrudService.save(configModel);
    }

    @PutMapping("/configs")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ConfigModel> update(@RequestBody ConfigModel configModel) {
        return configCrudService.update(configModel);
    }

    @DeleteMapping("/configs/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteByConfigKey(@PathVariable String key) {
        return configCrudService.deleteByConfigKey(key);
    }

    @GetMapping("/configs/{key}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ConfigModel> findByConfigKey(@PathVariable String key) {
        return configCrudService.findByConfigKey(key);
    }
}
