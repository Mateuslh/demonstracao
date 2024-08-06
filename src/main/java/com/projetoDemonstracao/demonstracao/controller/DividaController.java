package com.projetoDemonstracao.demonstracao.controller;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import com.projetoDemonstracao.demonstracao.service.DividaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dividas")
public class DividaController {

    @Autowired
    private DividaService dividaService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<Divida>>> findAll() {
        List<Divida> dividas = dividaService.findAll();
        ResponseDto<List<Divida>> response = new ResponseDto<>(true, null, dividas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Divida>> findById(@PathVariable Long id) {
        Divida divida = dividaService.findById(id);
        ResponseDto<Divida> response = new ResponseDto<>(true, null, divida);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Divida>> create(@RequestBody Divida divida) {
        Divida novaDivida = dividaService.save(divida);
        ResponseDto<Divida> response = new ResponseDto<>(true, "Dívida criada com sucesso", novaDivida);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Divida>> update(@PathVariable Long id, @RequestBody Divida divida) {
        divida.setId(id);
        Divida dividaAtualizada = dividaService.save(divida);
        ResponseDto<Divida> response = new ResponseDto<>(true, "Dívida atualizada com sucesso", dividaAtualizada);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> delete(@PathVariable Long id) {
        Divida divida = dividaService.findById(id);
        dividaService.delete(divida);
        ResponseDto<Void> response = new ResponseDto<>(true, "Dívida deletada com sucesso", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/debito")
    public ResponseEntity<ResponseDto<Debito>> findDebitoByDivida(@PathVariable Long id) {
        Divida divida = dividaService.findById(id);
        Debito debito = dividaService.findDebitoByDivida(divida);
        ResponseDto<Debito> response = new ResponseDto<>(true, null, debito);
        return ResponseEntity.ok(response);
    }
}
