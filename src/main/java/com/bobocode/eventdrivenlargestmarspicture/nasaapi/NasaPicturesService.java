package com.bobocode.eventdrivenlargestmarspicture.nasaapi;

import com.bobocode.eventdrivenlargestmarspicture.exception.PictureException;
import com.bobocode.eventdrivenlargestmarspicture.service.PictureService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NasaPicturesService {

    private final RestTemplate restTemplate;

    @Value("${nasa.base.url}")
    private String nasaUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    public byte[] findLargestPicture(String cameraName, Integer sol) {
        URI uri = UriComponentsBuilder.fromUriString(nasaUrl)
                .queryParam("sol", sol)
                .queryParam("api_key", apiKey)
                .queryParam("camera", cameraName)
                .build()
                .toUri();

        return Objects.requireNonNull(restTemplate.getForObject(uri, JsonNode.class))
                .findValuesAsText("img_src")
                .parallelStream()
                .map(this::urlToSize)
                .max(Map.Entry.comparingByValue())
                .map(entry -> restTemplate.getForObject(entry.getKey(), byte[].class))
                .orElseThrow(() -> new PictureException("No images were found with such params"));

    }

    private Map.Entry<String, Long> urlToSize(String imgUrl) {
        var responseEntity = restTemplate.exchange(imgUrl, HttpMethod.HEAD, null, void.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Map.entry(imgUrl, responseEntity.getHeaders().getContentLength());
        }
        if (responseEntity.getStatusCode().is3xxRedirection()) {
            return urlToSize(String.valueOf(responseEntity.getHeaders().getLocation()));
        }
        throw new PictureException("Wrong image url: " + imgUrl);
    }

}