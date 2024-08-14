package com.projetoDemonstracao.demonstracao.domain;

import com.projetoDemonstracao.demonstracao.enums.TipoCadastro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditoTributario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String abreviatura;
    @Column(nullable = false, unique = true)
    private String descricao;
    @Column(nullable = false)
    private boolean emUso;
    @Enumerated(EnumType.STRING)
    private TipoCadastro tipoCadastro;

}
