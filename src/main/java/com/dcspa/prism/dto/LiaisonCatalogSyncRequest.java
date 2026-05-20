package com.dcspa.prism.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Synchronisation groupée des affectations catalogue ↔ centre. */
@Getter
@Setter
public class LiaisonCatalogSyncRequest {
    private Integer idCentre;
    private List<Integer> createCatalogIds = new ArrayList<>();
    private List<Integer> deleteLiaisonIds = new ArrayList<>();
    private List<LiaisonCatalogUpdateRequest> updates = new ArrayList<>();
}
