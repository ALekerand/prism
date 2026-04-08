package com.dcspa.prism.service;

import com.dcspa.prism.repository.AlphaRepository;
import com.dcspa.prism.repository.AppRoleRepository;
import com.dcspa.prism.repository.AppUserRepository;
import com.dcspa.prism.repository.CecRepository;
import com.dcspa.prism.repository.CentreRepository;
import com.dcspa.prism.repository.CpRepository;
import com.dcspa.prism.repository.PersonnelRepository;
import com.dcspa.prism.repository.SieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final CentreRepository centreRepository;
    private final AlphaRepository alphaRepository;
    private final CecRepository cecRepository;
    private final CpRepository cpRepository;
    private final SieRepository sieRepository;
    private final PersonnelRepository personnelRepository;
    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;

    // Agrège les totaux affichés sur le tableau de bord admin.
    public Map<String, Object> buildSummary() {
        return Map.of(
                "centresTotal", centreRepository.count(),
                "alphaTotal", alphaRepository.count(),
                "cecTotal", cecRepository.count(),
                "cpTotal", cpRepository.count(),
                "sieTotal", sieRepository.count(),
                "personnelTotal", personnelRepository.count(),
                "usersTotal", appUserRepository.count(),
                "rolesTotal", appRoleRepository.count()
        );
    }
}
