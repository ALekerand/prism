package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Size(max = 50)
    @Column(name = "CODE_NIVEAU_ALPHA", length = 50)
    private String codeNiveauAlpha;

    @Size(max = 100)
    @Column(name = "LIBELLE_NIVEAU_ALPHA", length = 100)
    private String libelleNiveauAlpha;

    @JsonIgnore
    @OneToMany(mappedBy = "idNiveauAlpha")
    private Set<EffectifAlpha> effectifsAlpha = new HashSet<>();


}