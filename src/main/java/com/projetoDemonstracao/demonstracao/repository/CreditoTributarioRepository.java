package com.projetoDemonstracao.demonstracao.repository;

import com.projetoDemonstracao.demonstracao.domain.CreditoTributario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoTributarioRepository extends JpaRepository<CreditoTributario, Long> {
}
