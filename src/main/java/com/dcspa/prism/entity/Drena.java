package com.dcspa.prism.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "drena")
public class Drena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DRENA", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_DRENA", length = 10)
    private String codeDrena;

    @Size(max = 30)
    @Column(name = "NOM_DRENA", length = 30)
    private String nomDrena;

    @Size(max = 30)
    @Column(name = "MAIL_DRENA", length = 30)
    private String mailDrena;

    @Size(max = 15)
    @Column(name = "TELEPHONE_DRENA", length = 15)
    private String telephoneDrena;


}