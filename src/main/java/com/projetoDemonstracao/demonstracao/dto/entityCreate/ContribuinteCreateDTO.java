package com.projetoDemonstracao.demonstracao.dto.entityCreate;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContribuinteCreateDTO {

    private Long codigo;

    @Enumerated(EnumType.STRING)
    private com.projetoDemonstracao.demonstracao.enums.SituacaoCadastro SituacaoCadastro;

    private String nome;

    private String email;

    public Contribuinte toContribuinte() {
        Contribuinte contribuinte = new Contribuinte();
        contribuinte.setCodigo(codigo);
        contribuinte.setSituacaoCadastro(SituacaoCadastro);
        contribuinte.setNome(nome);
        contribuinte.setEmail(email);
        return contribuinte;
    }
}
