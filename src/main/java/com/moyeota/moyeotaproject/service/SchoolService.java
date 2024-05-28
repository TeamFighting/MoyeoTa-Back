package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.dto.UsersDto.SchoolDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolService {

    private static final int MAX_PAGES = 25;

    @Value("${school.api.url}")
    private String schoolApiUrl;

    @Value("${school.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public List<SchoolDto.SchoolInfo> searchSchool() {
        return IntStream.range(1, MAX_PAGES)
                .mapToObj(this::fetchSchoolData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(schoolJson -> schoolJson.getDataSearch().getContent().stream())
                .map(school -> SchoolDto.SchoolInfo.builder()
                        .name(school.getSchoolName())
                        .build())
                .collect(toList());
    }

    private Optional<SchoolJson> fetchSchoolData(int page) {
        try {
            String requestUrl = String.format("%s?apiKey=%s&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&thisPage=%d",
                    schoolApiUrl, apiKey, page);
            log.info("Fetching data from URL: {}", requestUrl);
            return Optional.ofNullable(restTemplate.getForObject(requestUrl, SchoolJson.class));
        } catch (Exception e) {
            log.error("Error fetching data for page {}: {}", page, e.getMessage());
            return Optional.empty();
        }
    }
}
