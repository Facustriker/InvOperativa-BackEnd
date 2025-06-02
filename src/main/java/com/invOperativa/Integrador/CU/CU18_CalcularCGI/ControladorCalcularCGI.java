package com.invOperativa.Integrador.CU.CU18_CalcularCGI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/CalcularCGI")
public class ControladorCalcularCGI {

    @Autowired
    protected ExpertoCalcularCGI experto;

    @GetMapping("/calculo")
    public ResponseEntity<?> calculoCGI(@RequestParam Long idArticulo) {
        DTOCalcularCGI ret = experto.calculoCGI(idArticulo);
        return ResponseEntity.ok(ret);
    }
}
