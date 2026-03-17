package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "iep")
public class Iep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_IEP", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DRENA", nullable = false)
    private Drena idDrena;

    @Size(max = 10)
    @Column(name = "CODE_IEP", length = 10)
    private String codeIep;

    @Size(max = 30)
    @Column(name = "NOM_IEP", length = 30)
    private String nomIep;

    @Size(max = 30)
    @Column(name = "MAIL_IEP", length = 30)
    private String mailIep;

    @Size(max = 10)
    @Column(name = "TELEPHONE_IEP", length = 10)
    private String telephoneIep;


}