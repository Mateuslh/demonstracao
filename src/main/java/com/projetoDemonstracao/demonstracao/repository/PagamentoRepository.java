package com.projetoDemonstracao.demonstracao.repository;

import com.projetoDemonstracao.demonstracao.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
