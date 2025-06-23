package com.invOperativa.Integrador.CU.CU8_CancelarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODatosModificacion;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/CancelarOrdenDeCompra")
public class ControladorCancelarOrdenDeCompra {

    @Autowired
    protected ExpertoCancelarOrdenDeCompra experto;

    @GetMapping("/getEstadoOC")
    public ResponseEntity<?> getEstadoOC(@RequestParam Long idOC) {
        DTOEstadoOC ret = experto.getEstadoOC(idOC);
        return ResponseEntity.ok(ret);
    }

    @PutMapping("/cancelarOrden")
    public ResponseEntity<?> cancelarOrden(@RequestParam Long idOC) {
        experto.cancelarOC(idOC);
        return ResponseEntity.ok().build();
    }
}
