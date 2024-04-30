package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeota.moyeotaproject.dto.UsersDto.SchoolDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolService {

    private final String schoolApiUrl = "http://www.career.go.kr/cnet/openapi/getOpenApi.xml";
    private final String apiKey = "15495ecea92f26bae8712b88f11e02e3";
    private final RestTemplate restTemplate;

    public List<SchoolDto.schoolInfo> searchSchool() {
        List<SchoolDto.schoolInfo> result = new ArrayList<>();
        for (int page = 1; page < 25; page++) {
            String requestUrl = String.format("%s?apiKey=%s&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&page=%s", schoolApiUrl, apiKey, page);
            log.info("requestUrl = {}", requestUrl);
            SchoolJson schoolJson = restTemplate.getForObject(requestUrl, SchoolJson.class);
            schoolJson.getDataSearch().getContent().forEach(school -> {
                result.add(SchoolDto.schoolInfo.builder()
                        .name(school.getSchoolName())
                        .build());
            });
        }
        return result;
    }
}
