package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ABMArticulo")
public class ControladorABMArticulo {

    @Autowired
    private ExpertoABMArticulo experto;

    // Dar de alta nuevo articulo
    @PostMapping("/alta")
    public ResponseEntity<?> alta(@RequestParam Articulo art) {
        experto.altaArticulo(art);
        return ResponseEntity.ok().build();
    }

}
