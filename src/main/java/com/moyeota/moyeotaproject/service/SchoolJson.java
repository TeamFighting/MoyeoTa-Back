package com.moyeota.moyeotaproject.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolJson {

    private DataSearch dataSearch;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataSearch {
        private List<Content> content;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        private String schoolName;
    }
}
