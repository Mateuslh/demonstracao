package com.projetoDemonstracao.demonstracao.domain;

import com.projetoDemonstracao.demonstracao.enums.SituacaoCadastro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contribuinte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, unique = true)
    private Long id;

    private Long codigo;

    @Enumerated(EnumType.STRING)
    private SituacaoCadastro SituacaoCadastro;

    private String nome;

    private String email;
}
