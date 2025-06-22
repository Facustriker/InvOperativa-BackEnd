package com.invOperativa.Integrador.CU.CU8_CancelarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODatosModificacion;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/CancelarOrdenDeCompra")
public class ControladorCancelarOrdenDeCompra {

    @Autowired
    protected ExpertoCancelarOrdenDeCompra experto;

    @GetMapping("/getDatosOC")
    public ResponseEntity<?> getDatosOC(@RequestParam Long idOC) {
        DTOModificarOrdenCompra ret = experto.getDatosOC(idOC);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/cancelarOrden")
    public ResponseEntity<?> cancelarOrden(@RequestBody DTODatosModificacion dto) {
        experto.cancelarOrden();
        return ResponseEntity.ok().build();
    }
}
