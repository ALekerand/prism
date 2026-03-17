package com.dcspa.prism.codegen;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "code_sequence")
public class CodeSequence {

    @Id
    @Column(name = "PREFIX", nullable = false, length = 10)
    private String prefix;

    @Column(name = "NEXT_VALUE", nullable = false)
    private long nextValue;
}

