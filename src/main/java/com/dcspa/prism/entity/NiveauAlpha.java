package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "niveau_alpha")
@AutoCode(field = "codeNiveauAlpha")
@EntityListeners(AutoCodeEntityListener.class)
public class NiveauAlpha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NIVEAU_ALPHA", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "CODE_NIVEAU_ALPHA", length = 50)
    private String codeNiveauAlpha;

    @Size(max = 100)
    @Column(name = "LIBELLE_NIVEAU_ALPHA", length = 100)
    private String libelleNiveauAlpha;

    @JsonIgnore
    @OneToMany(mappedBy = "idNiveauAlpha")
    private Set<EffectifAlpha> effectifsAlpha = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "idNiveauAlpha")
    private Set<AlphaNiveau> alphaNiveaux = new HashSet<>();

}