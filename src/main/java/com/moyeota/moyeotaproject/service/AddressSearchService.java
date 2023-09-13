package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.DocumentDto;
import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressSearchService {

    @Value("${KAKAO.REST.API-KEY}")
    private String apiKey;

    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private final RestTemplate restTemplate;

    public DocumentDto requestAddressSearch(String address) {
        if (ObjectUtils.isEmpty(address)) throw new IllegalArgumentException("해당 주소에 대한 정보가 없습니다. 주소 = " + address);
        URI uri = buildUriByAddressSearch(address);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        // kakao api 호출
        KakaoApiResponseDto kakaoApiResponseDto = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            throw new IllegalArgumentException("해당 주소에 대한 정보가 없습니다. 주소 =" + address);
        }
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0); // 제일 첫번째 정보를 가져온다
        return documentDto;
    }

    public URI buildUriByAddressSearch(String address) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriBuilder.queryParam("query", address);
        URI uri = uriBuilder.build().encode().toUri();
        return uri;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

    public String compareAddress(String address1, String address2) {
        DocumentDto documentDto1 = requestAddressSearch(address1);
        DocumentDto documentDto2 = requestAddressSearch(address2);
        double result = calculateDistance(documentDto1.getLatitude(), documentDto1.getLongitude(), documentDto2.getLatitude(), documentDto2.getLongitude());
        return roundDouble(result) + "km";
    }

    public String roundDouble(double result) {
        return String.format("%.2f", result);
    }
}
