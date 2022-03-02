package com.example.customerdebtservice.debt.controllers;

import com.example.customerdebtservice.debt.dto.DebtData;
import com.example.customerdebtservice.debt.forms.DebtForm;
import com.example.customerdebtservice.debt.services.DebtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DebtController {

    private final DebtService debtService;

    @GetMapping()
    public ResponseEntity<List<DebtData>> getDebts(Pageable pageable) {
        log.info("GET request: /debts");
        return ResponseEntity.ok().body(debtService.getDebts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebtData> getDebt(@PathVariable Long id) {
        log.info("GET request: /debts/" + id);
        return ResponseEntity.ok().body(debtService.getDebtById(id));
    }

    @PostMapping()
    public ResponseEntity<DebtData> createDebt(@Valid @RequestBody DebtForm debtForm,
                                               BindingResult bindingResult) {
        log.info("POST request: /debts");
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/debts").toUriString());
        return ResponseEntity.created(uri).body(debtService.createDebt(debtForm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DebtData> updateDebt(@PathVariable Long id,
                                               @Valid @RequestBody DebtForm debtForm,
                                               BindingResult bindingResult) {
        log.info("PUT request: /debts/" + id);
        return ResponseEntity.ok().body(debtService.updateDebt(id, debtForm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable Long id) {
        log.info("DELETE request: /debts/" + id);
        debtService.deleteDebt(id);
        return ResponseEntity.ok().build();
    }

}
