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
@Table(name = "ecole_tutrice")
@AutoCode(field = "codeEcoleTutrice")
@EntityListeners(AutoCodeEntityListener.class)
public class EcoleTutrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ECOLE_TUTRICE", nullable = false)
    private Integer id;

    @Size(max = 20)
    @Column(name = "CODE_ECOLE_TUTRICE", length = 20)
    private String codeEcoleTutrice;

    @Size(max = 150)
    @Column(name = "LIBELLE_ECOLE_TUTRICE", length = 150)
    private String libelleEcoleTutrice;
}
