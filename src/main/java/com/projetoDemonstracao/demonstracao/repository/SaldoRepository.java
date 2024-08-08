package com.projetoDemonstracao.demonstracao.repository;

import com.projetoDemonstracao.demonstracao.domain.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {

}
