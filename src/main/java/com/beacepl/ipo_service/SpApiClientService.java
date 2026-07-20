package com.beacepl.ipo_service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpApiClientService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${sp.service.base-url}")
    private String baseUrl;

    public List<IpoInfo> getIpoList(String investorCode, String date)
            throws IOException, InterruptedException {

        String url = baseUrl + "/sp/ipo-info?investorCode=" +
                URLEncoder.encode(investorCode, StandardCharsets.UTF_8) +
                "&date=" +
                URLEncoder.encode(date, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(
                response.body(),
                new TypeReference<List<IpoInfo>>() {}
        );
    }

    public List<IpoInfo> getIpoListAdmin(String date)
            throws IOException, InterruptedException {

        String url = baseUrl + "/sp/ipo-info-list?date=" +
                URLEncoder.encode(date, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(
                response.body(),
                new TypeReference<List<IpoInfo>>() {}
        );
    }

    public IpoResponse applyForIpo(ApplyIpoDto applyIpoDto) {

        String url = baseUrl + "/sp/ipo-apply";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApplyIpoDto> request =
                new HttpEntity<>(applyIpoDto, headers);

        ResponseEntity<IpoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                IpoResponse.class
        );

        return response.getBody();
    }
}