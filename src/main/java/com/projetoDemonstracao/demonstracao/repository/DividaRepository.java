package com.projetoDemonstracao.demonstracao.repository;

import com.projetoDemonstracao.demonstracao.domain.Divida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividaRepository extends JpaRepository<Divida, Long> {
    List<Divida> findAllByDebitoOrigem_Contribuinte_Id(Long id);
}
