package com.invOperativa.Integrador.CU.CU12_AjustarInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/AjustarInventario")
public class ControladorAjustarInventario {

    @Autowired
    protected ExpertoAjustarInventario experto;

    @GetMapping("/getArticulo")
    public ResponseEntity<?> getArticulo(@RequestParam Long idArticulo) {
        DTOAjustarInventario ret = experto.getArticulo(idArticulo);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestParam Long idArticulo, @RequestParam int stock) {
        boolean ret = experto.confirmar(idArticulo,stock);
        return ResponseEntity.ok(ret);
    }
}
