package com.example.controller;

import com.example.domain.dto.DataUpdateRequestDto;
import com.example.domain.dto.SubscriberDataUpdateResponseDto;
import com.example.domain.service.SubscriberDataUpdateService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class SubscriberDataUpdateController {
    private final SubscriberDataUpdateService _subscriberDataUpdate;

    public SubscriberDataUpdateController(SubscriberDataUpdateService subscriberDataUpdate) {
        this._subscriberDataUpdate = subscriberDataUpdate;
    }

    /**
     * Контпроллер очень прост в реализации:
     * получает запрос, трансформирует запрос и передает сервису бизнес-логики,
     * получает ответ в виде Result<success, failure> и трансформирует
     * в ответ по контракту REST API
     */
//    /subscriber-data-updates - антипаттерн RESTful
    @PutMapping("/subscriber-data-updates")
    public Mono<RestResponse<SubscriberDataUpdateResponseDto>> subscriberDataUpdate(@RequestBody RestRequest<DataUpdateRequestDto> subscriberDataUpdate) {
        return _subscriberDataUpdate.subscriberUpdate()
    }
}