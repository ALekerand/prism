package com.dcspa.prism.entity;

import com.dcspa.prism.codegen.AutoCode;
import com.dcspa.prism.codegen.AutoCodeEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organisation_faitiere")
@AutoCode(field = "codeOrganisationFaitiere")
@EntityListeners(AutoCodeEntityListener.class)
public class OrganisationFaitiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ORGANISATION_FAITIERE", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "CODE_ORGANISATION_FAITIERE", length = 20)
    private String codeOrganisationFaitiere;

    @Size(max = 250)
    @Column(name = "LIBELLE_ORGANISATION_FAITIERE", length = 250)
    private String libelleOrganisationFaitiere;

    @Size(max = 20)
    @Column(name = "SIGLE_ORGANISATION_FAITIERE", length = 20)
    private String sigleOrganisationFaitiere;

    @Size(max = 150)
    @Column(name = "POINT_FOCAL", length = 150)
    private String pointFocal;

    @Size(max = 150)
    @Column(name = "FONCTION_POINT_FOCAL", length = 150)
    private String fonctionPointFocal;

    @Size(max = 250)
    @Column(name = "CONTACTS", length = 250)
    private String contacts;

    @Size(max = 250)
    @Column(name = "COURRIEL", length = 250)
    private String courriel;
}
