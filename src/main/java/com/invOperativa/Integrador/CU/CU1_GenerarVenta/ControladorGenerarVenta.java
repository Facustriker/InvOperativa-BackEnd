package com.invOperativa.Integrador.CU.CU1_GenerarVenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/generarVenta")
public class ControladorGenerarVenta {

    @Autowired
    private ExpertoGenerarVenta experto;

    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody DTOGenerarVenta dto){
        experto.nueva(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/traerTodos")
    public ResponseEntity<?> traerTodos(){
        List<DTOVenta> ventas = experto.getAll();
        return ResponseEntity.ok(ventas);
    }

}
