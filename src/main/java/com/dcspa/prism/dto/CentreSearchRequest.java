package com.dcspa.prism.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CentreSearchRequest {
    private Map<String, String> criteria;
}
