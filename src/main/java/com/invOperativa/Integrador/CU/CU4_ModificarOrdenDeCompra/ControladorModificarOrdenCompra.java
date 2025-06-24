package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/ModificarOrdenCompra")
public class ControladorModificarOrdenCompra {

    @Autowired
    protected ExpertoModificarOrdenCompra experto;

    @GetMapping("/getDatosOC")
    public ResponseEntity<?> getDatosOC(@RequestParam Long idOC) {
        DTOModificarOrdenCompra ret = experto.getDatosOC(idOC);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody DTODatosModificacion dto) {
        experto.confirmar(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idOC}/detalle/{idOCDetalle}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long idOC, @PathVariable Long idOCDetalle) {
        experto.eliminarDetalle(idOC, idOCDetalle);
        return ResponseEntity.noContent().build();
    }
}
