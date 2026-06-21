package com.hyperalert.hyperalert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PincodeResponse {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("PostOffice")
    private List<PostOffice> postOffice;

    @Data
    public static class PostOffice {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("District")
        private String district;

        @JsonProperty("State")
        private String state;

        @JsonProperty("Pincode")
        private String pincode;
    }
}