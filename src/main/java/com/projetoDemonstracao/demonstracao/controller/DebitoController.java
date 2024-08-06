package com.projetoDemonstracao.demonstracao.controller;

import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import com.projetoDemonstracao.demonstracao.service.DebitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/debitos")
public class DebitoController {


    @Autowired
    private DebitoService debitoService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<Debito>>> findAll() {
        List<Debito> debitos = debitoService.findAll();
        ResponseDto<List<Debito>> response = new ResponseDto<>(true, null, debitos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Debito>> findById(@PathVariable Long id) {
        Debito debito = debitoService.findById(id);
        ResponseDto<Debito> response = new ResponseDto<>(true, null, debito);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Debito>> create(@RequestBody Debito debito) {
        Debito novoDebito = debitoService.save(debito);
        ResponseDto<Debito> response = new ResponseDto<>(true, "Debito criado com sucesso", novoDebito);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Debito>> update(@PathVariable Long id, @RequestBody Debito debito) {
        debito.setId(id);
        Debito debitoAtualizado = debitoService.save(debito);
        ResponseDto<Debito> response = new ResponseDto<>(true, "Debito atualizado com sucesso", debitoAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> delete(@PathVariable Long id) {
        Debito debito = debitoService.findById(id);
        debitoService.delete(debito);
        ResponseDto<Void> response = new ResponseDto<>(true, "Debito deletado com sucesso", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contribuinte/{contribuinteId}")
    public ResponseEntity<ResponseDto<List<Debito>>> findByContribuinteId(@PathVariable Long contribuinteId) {
        List<Debito> debitos = debitoService.findByContribuinteId(contribuinteId);
        ResponseDto<List<Debito>> response = new ResponseDto<>(true, null, debitos);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/inscrever/{id}")
    public ResponseEntity<ResponseDto<Divida>> inscreverDebito(@PathVariable Long id) {
        Debito debito = debitoService.findById(id);
        Divida divida = debitoService.inscreverDebito(debito);
        ResponseDto<Divida> response = new ResponseDto<>(true, "Debito inscrito com sucesso", divida);
        return ResponseEntity.ok(response);
    }

}
