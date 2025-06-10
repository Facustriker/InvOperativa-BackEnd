package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/GenerarOrdenCompra")
public class ControladorGenerarOrdenDeCompra {

    @Autowired
    public ExpertoGenerarOrdenDeCompra experto;

    @GetMapping("/sugerirOrden")
    public ResponseEntity<?> get(List<Long> ids){
        DTOSugerirOrden sugerencias = experto.sugerirOrden(ids);
        return ResponseEntity.ok(sugerencias);
    }

    @PostMapping("/nuevaOrden")
    public ResponseEntity<?> post(DTONuevaOrden dto){
        List<String> yaPedidos = experto.nuevaOrden(dto);
        return ResponseEntity.ok(yaPedidos);
    }

}
