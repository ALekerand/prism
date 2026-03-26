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
@Table(name = "periodicite")
@AutoCode(field = "codePeriodicite")
@EntityListeners(AutoCodeEntityListener.class)
public class Periodicite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERIODICITE", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_PERIODICITE", length = 10)
    private String codePeriodicite;

    @Size(max = 15)
    @Column(name = "LIBELLE_PERIODICITE", length = 15)
    private String libellePeriodicite;


}