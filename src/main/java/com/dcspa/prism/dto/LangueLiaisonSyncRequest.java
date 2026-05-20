package com.dcspa.prism.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LangueLiaisonSyncRequest {
    private Integer idCentre;
    private List<String> createLabels = new ArrayList<>();
    private List<Integer> deleteLiaisonIds = new ArrayList<>();
}
