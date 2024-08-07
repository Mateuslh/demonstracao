package com.projetoDemonstracao.demonstracao.controller;

import com.projetoDemonstracao.demonstracao.domain.Pagamento;
import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import com.projetoDemonstracao.demonstracao.service.PagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<Pagamento>>> findAll() {
        List<Pagamento> pagamentos = pagamentoService.findAll();
        ResponseDto<List<Pagamento>> response = new ResponseDto<>(true, null, pagamentos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Pagamento>> findById(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.findById(id);
        ResponseDto<Pagamento> response = new ResponseDto<>(true, null, pagamento);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Pagamento>> create(@RequestBody Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.save(pagamento);
        ResponseDto<Pagamento> response = new ResponseDto<>(true, "Pagamento criado com sucesso", novoPagamento);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> delete(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.findById(id);
        pagamentoService.delete(pagamento);
        ResponseDto<Void> response = new ResponseDto<>(true, "Pagamento deletado com sucesso", null);
        return ResponseEntity.ok(response);
    }
}
