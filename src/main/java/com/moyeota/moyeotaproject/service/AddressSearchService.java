package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.AddressDto;
import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.DocumentDto;
import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.DurationAndFareResponseDto;
import com.moyeota.moyeotaproject.controller.dto.KakaoApiDto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressSearchService {

    @Value("${KAKAO.REST.API-KEY}")
    private String apiKey;

    private static final String KAKAO_DURATION_FARE_URL = "https://apis-navi.kakaomobility.com/v1/directions";
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
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

    public DurationAndFareResponseDto getDurationAndFare(String origin, String destination) throws ParseException {
        if(origin == null || origin.equals(""))
            throw new IllegalArgumentException("출발지 정보가 없습니다.");
        if(destination == null || destination.equals(""))
            throw new IllegalArgumentException("목적지 정보가 없습니다.");

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_DURATION_FARE_URL);
        uriComponentsBuilder.queryParam("origin", origin);
        uriComponentsBuilder.queryParam("destination", destination);
        URI uri = uriComponentsBuilder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        headers.set("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity<>(headers);

        String result =  restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

        JSONParser parser = new JSONParser();

        JSONObject object = (JSONObject) parser.parse(result);
        JSONArray routes = (JSONArray) object.get("routes");
        int taxi = 0;
        int duration = 0;
        for (int i=0; i<routes.size(); i++){
            object = (JSONObject) routes.get(i);
            JSONObject summary = (JSONObject) object.get("summary");
            JSONObject fare = (JSONObject) summary.get("fare");
            taxi = Integer.parseInt(fare.get("taxi").toString());
            JSONArray sections = (JSONArray) object.get("sections");
            for (int j=0; j<sections.size(); j++){
                object = (JSONObject) sections.get(j);
                duration = Integer.parseInt(object.get("duration").toString());
            }
        }

        DurationAndFareResponseDto durationAndFareResponseDto = new DurationAndFareResponseDto(taxi, duration);
        log.info("durationAndFareResponseDto: " + durationAndFareResponseDto);
        if (Objects.isNull(durationAndFareResponseDto)) {
            throw new IllegalArgumentException("해당 경로에 대한 정보가 없습니다. 출발지=" + origin + ", 목적지=" + destination);
        }
        return durationAndFareResponseDto;
    }

    public AddressDto findAddress(String keyword) throws ParseException {
        if(keyword == null || keyword.equals(""))
            throw new IllegalArgumentException("키워드 정보가 없습니다.");

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_KEYWORD_SEARCH_URL);
        uriComponentsBuilder.queryParam("query", keyword);
        URI uri = uriComponentsBuilder.build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        String result =  restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

        JSONParser parser = new JSONParser();

        JSONObject object = (JSONObject) parser.parse(result);
        JSONArray documents = (JSONArray) object.get("documents");
        for (int i=0; i<documents.size(); i++){
            object = (JSONObject) documents.get(i);
            if(object.get("place_name").equals(keyword))
                return new AddressDto((String) object.get("place_name"), (String) object.get("address_name"), (String) object.get("road_address_name"), (String) object.get("x"), (String) object.get("y"));
        }

        return null;
    }
}
