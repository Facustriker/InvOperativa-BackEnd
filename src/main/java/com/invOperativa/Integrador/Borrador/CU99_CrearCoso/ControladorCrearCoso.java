package com.invOperativa.Integrador.Borrador.CU99_CrearCoso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/CrearCoso")
public class ControladorCrearCoso {
    @Autowired
    protected ExpertoCrearCoso experto;

    @PostMapping(value = "/crear")
    public ResponseEntity<?> crear(@RequestBody DTOCrearCoso request) {
        try {
            Long ret = experto.crear(request);
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
