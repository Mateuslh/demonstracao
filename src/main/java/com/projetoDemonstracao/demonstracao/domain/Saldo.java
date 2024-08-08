package com.projetoDemonstracao.demonstracao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Saldo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private BigDecimal valor;
    private LocalDateTime dhLancamento = LocalDateTime.now();
    private LocalDate dtValidade;
    private String mensagem;

    @ManyToOne
    private Pagamento pagamento;
}
