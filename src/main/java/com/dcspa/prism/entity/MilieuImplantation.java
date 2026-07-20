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
@Table(name = "milieu_implantation")
@AutoCode(field = "codeMilieuImplentation")
@EntityListeners(AutoCodeEntityListener.class)
public class MilieuImplantation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MILIEU_IMPLENTATION", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "CODE_MILIEU_IMPLENTATION", length = 10)
    private String codeMilieuImplentation;

    @Size(max = 10)
    @Column(name = "LIBELLE_TYPE_IMPLENTATION_", length = 10)
    private String libelleTypeImplentation;

}
