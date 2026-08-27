package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "localite_d_implantation")
@AutoCode(field = "codeLocalite")
@EntityListeners(AutoCodeEntityListener.class)
public class LocaliteDImplantation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOCALITE", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SOUS_PREFECTURE", nullable = false)
    private SousPrefecture idSousPrefecture;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MILIEU_IMPLENTATION", nullable = false)
    private MilieuImplantation idMilieuImplentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMMUNE")
    private Commune idCommune;

    @Size(max = 10)
    @Column(name = "CODE_LOCALITE", length = 10)
    private String codeLocalite;

    @Size(max = 30)
    @Column(name = "NOM_LOCALITE", length = 30)
    private String nomLocalite;


}