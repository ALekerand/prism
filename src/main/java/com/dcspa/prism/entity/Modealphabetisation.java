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
@Table(name = "modealphabetisation")
@AutoCode(field = "codeModealpha")
@EntityListeners(AutoCodeEntityListener.class)
public class Modealphabetisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MODEALPHA", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CENTRE", nullable = false)
    private Alpha idCentre;

    @Size(max = 50)
    @Column(name = "CODE_MODEALPHA", length = 50)
    private String codeModealpha;

    @Size(max = 100)
    @Column(name = "LIBELLE_MODEALPHA", length = 100)
    private String libelleModealpha;


}