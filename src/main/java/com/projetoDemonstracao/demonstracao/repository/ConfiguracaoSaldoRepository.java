package com.projetoDemonstracao.demonstracao.repository;

import com.projetoDemonstracao.demonstracao.domain.configsAplica.ConfiguracaoSaldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracaoSaldoRepository extends JpaRepository<ConfiguracaoSaldo, Long> {

    ConfiguracaoSaldo findFirstByOrderByIdDesc();
}
