package com.projetoDemonstracao.demonstracao.controller;

import com.projetoDemonstracao.demonstracao.domain.Contribuinte;
import com.projetoDemonstracao.demonstracao.domain.Debito;
import com.projetoDemonstracao.demonstracao.domain.Divida;
import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import com.projetoDemonstracao.demonstracao.dto.entityCreate.ContribuinteCreateDTO;
import com.projetoDemonstracao.demonstracao.service.ContribuinteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/contribuintes")
@Tag(name = "Contribuientes")
public class ContribuinteController {


    @Autowired
    private ContribuinteService contribuinteService;

    @Operation(summary = "Busca todos os Contribuintes")
    @GetMapping
    public ResponseEntity<ResponseDto<List<Contribuinte>>> findAll() {
        List<Contribuinte> contribuintes = contribuinteService.findAll();
        ResponseDto<List<Contribuinte>> response = new ResponseDto<>(true, null, contribuintes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Contribuinte>> findById(
            @Parameter(description = "id do Contribuinte") @PathVariable() Long id) {
        Contribuinte contribuinte = contribuinteService.findById(id);
        ResponseDto<Contribuinte> response = new ResponseDto<>(true, null, contribuinte);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Contribuinte>> create(@RequestBody ContribuinteCreateDTO contribuinteDTO) {
        Contribuinte contribuinte = contribuinteDTO.toContribuinte();
        Contribuinte novoContribuinte = contribuinteService.save(contribuinte);
        ResponseDto<Contribuinte> response = new ResponseDto<>(true, "Contribuinte criado com sucesso", novoContribuinte);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Contribuinte>> update(@PathVariable Long id, @RequestBody ContribuinteCreateDTO contribuinteCreateDTO) {
        Contribuinte contribuinte = contribuinteCreateDTO.toContribuinte();
        contribuinte.setId(id);
        Contribuinte contribuinteAtualizado = contribuinteService.save(contribuinte);
        ResponseDto<Contribuinte> response = new ResponseDto<>(true, "Contribuinte atualizado com sucesso", contribuinteAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> delete(@PathVariable Long id) {
        Contribuinte contribuinte = contribuinteService.findById(id);
        contribuinteService.delete(contribuinte);
        ResponseDto<Void> response = new ResponseDto<>(true, "Contribuinte deletado com sucesso", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/debitos")
    public ResponseEntity<ResponseDto<List<Debito>>> findAllDebitos(@PathVariable Long id) {
        Contribuinte contribuinte = contribuinteService.findById(id);
        List<Debito> debitos = contribuinteService.findAllDebitos(contribuinte);
        ResponseDto<List<Debito>> response = new ResponseDto<>(true, null, debitos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/dividas")
    public ResponseEntity<ResponseDto<List<Divida>>> findAllDividas(@PathVariable Long id) {
        Contribuinte contribuinte = contribuinteService.findById(id);
        List<Divida> dividas = contribuinteService.findAllDividas(contribuinte);
        ResponseDto<List<Divida>> response = new ResponseDto<>(true, null, dividas);
        return ResponseEntity.ok(response);
    }

}
