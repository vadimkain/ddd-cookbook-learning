package com.example.domain;

import com.example.controller.RestResponse;
import com.example.domain.common.Result;
import com.example.domain.dto.DataUpdateDto;
import com.example.domain.dto.SubscriberDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@Component
public class SubscriberRestClient {
    private final WebClient webClient;
    @Value("${rest.services.subscribers.updates}")
    private String dataUpdatesUrl;
    @Value("${rest.services.subscribers.subscribers}")
    private String subscribersUrl;

    public SubscriberRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * ParameterizedTypeReference используется для указания типа, в который будет десериализован JSON-ответ.
     * В данном случае это RestResponse<DataUpdateDto> и RestResponse<SubscriberDto> соответственно.
     * Этот класс позволяет избежать проблем с потерей информации о типе во время выполнения.
     **/
    public Mono<Result<RestResponse<DataUpdateDto>>> findDataUpdate(String dataUpdateId) {
        // Создаем типизированную ссылку для десериализации ответа
        ParameterizedTypeReference<RestResponse<DataUpdateDto>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(dataUpdatesUrl, dataUpdateId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference)
                .map(Result::success)
                .onErrorResume(exception -> Mono.just(Result.failure(exception)));
    }

    public Mono<Result<RestResponse<SubscriberDto>>> findSubscriber(String subscriberId) {
        // Создаем типизированную ссылку для десериализации ответа
        ParameterizedTypeReference<RestResponse<SubscriberDto>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(subscribersUrl, subscriberId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference)
                .map(Result::success)
                .onErrorResume(exception -> Mono.just(Result.failure(exception)));
    }
}
