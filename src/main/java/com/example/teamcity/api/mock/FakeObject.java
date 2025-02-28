package com.example.teamcity.api.mock;

import com.example.teamcity.api.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeObject extends BaseModel {
    @Builder.Default
    private String id = "fake-id";
    @Builder.Default
    private String name = "fake-name";
}
