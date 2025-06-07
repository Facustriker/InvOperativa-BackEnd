package com.invOperativa.Integrador.CU.CU6_EnviarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODatosModificacion;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/EnviarOrdenCompra")
public class ControladorEnviarOrdenCompra {

    @Autowired
    protected ExpertoEnviarOrdenCompra experto;

    @GetMapping("/getDatosOC")
    public ResponseEntity<?> getDatosOC(@RequestParam Long idOC) {
        DTOEnviarOrdenCompra ret = experto.getDatosOC(idOC);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestParam Long idOC) {
        experto.confirmar(idOC);
        return ResponseEntity.ok().build();
    }
}
