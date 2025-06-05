package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/FinalizarOrdenCompra")
public class ControladorFinalizarOrdenCompra {

    @Autowired
    protected ExpertoFinalizarOrdenCompra experto;

    @GetMapping("/getDatosOC")
    public ResponseEntity<?> getDatosOC(@RequestParam Long idOC) {
        DTOFinalizarOrdenCompra ret = experto.getDatosOC(idOC);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestParam Long idOC) {
        DTORespuestaFinalizarOC respuesta = experto.confirmar(idOC);
        return ResponseEntity.ok(respuesta);
    }
}
