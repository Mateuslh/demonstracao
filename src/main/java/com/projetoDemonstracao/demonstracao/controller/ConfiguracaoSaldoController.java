package com.projetoDemonstracao.demonstracao.controller;

import com.projetoDemonstracao.demonstracao.domain.configsAplica.ConfiguracaoSaldo;
import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import com.projetoDemonstracao.demonstracao.dto.entityCreate.ConfiguracaoSaldoCreateDTO;
import com.projetoDemonstracao.demonstracao.service.ConfiguracaoSaldoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ConfiguracaoSaldo")
@Tag(name = "Configuracao saldo")
public class ConfiguracaoSaldoController {

    @Autowired
    private ConfiguracaoSaldoService ConfiguracaoSaldoService;

    @GetMapping("/findFirst")
    public ResponseEntity<ResponseDto<ConfiguracaoSaldo>> findFirst() {
        ConfiguracaoSaldo configuracaoSaldo = ConfiguracaoSaldoService.findFirst();
        ResponseDto<ConfiguracaoSaldo> response = new ResponseDto<ConfiguracaoSaldo>(true, null, configuracaoSaldo);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ResponseDto<ConfiguracaoSaldo>> create(@RequestBody ConfiguracaoSaldoCreateDTO configuracaoSaldoCreateDTO) {
        ConfiguracaoSaldo configuracaoSaldo = configuracaoSaldoCreateDTO.toEntity();
        ConfiguracaoSaldo novaConfiguracaoSaldo = ConfiguracaoSaldoService.save(configuracaoSaldo);
        ResponseDto<ConfiguracaoSaldo> response = new ResponseDto<ConfiguracaoSaldo>(true, "Configuracao criada com sucesso", novaConfiguracaoSaldo);
        return ResponseEntity.ok(response);
    }

}
