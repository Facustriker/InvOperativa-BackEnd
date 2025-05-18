package com.invOperativa.Integrador.CU.CU15_ABMInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

@RestController
@RequestMapping(path = "/inventario")
public class ControladorABMInventario {

    @Autowired
    protected ExpertoABMInventario experto;

    @GetMapping("/")
    public ResponseEntity<Collection<DTOInventario>> getAll() {
        Collection<DTOInventario> inv = experto.getAll();

        return ResponseEntity.ok(inv);
    }
}
