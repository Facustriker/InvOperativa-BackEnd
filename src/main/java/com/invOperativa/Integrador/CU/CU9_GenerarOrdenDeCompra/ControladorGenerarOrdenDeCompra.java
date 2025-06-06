package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/GenerarOrdenCompra")
public class ControladorGenerarOrdenDeCompra {

    @Autowired
    public ExpertoGenerarOrdenDeCompra experto;

    @PostMapping("/nuevaOrde")
    public ResponseEntity<?> post(DTONuevaOrden dto){
        experto.nuevaOrden(dto);
        return ResponseEntity.ok().build();
    }

}
