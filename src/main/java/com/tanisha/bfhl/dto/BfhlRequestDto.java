package com.tanisha.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BfhlRequestDto {

    @NotNull(message = "data field must not be null")
    @JsonProperty("data")
    private List<String> data;
}
