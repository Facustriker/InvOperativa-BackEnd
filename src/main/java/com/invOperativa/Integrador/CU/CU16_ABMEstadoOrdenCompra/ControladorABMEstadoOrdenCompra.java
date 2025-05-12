package com.invOperativa.Integrador.CU.CU16_ABMEstadoOrdenCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ABMEstadoOrdenCompra")
public class ControladorABMEstadoOrdenCompra {

    @Autowired
    protected ExpertoABMEstadoOrdenCompra experto;

    @GetMapping("/getEstados")
    public ResponseEntity<?> getEstados(boolean soloVigentes) {
        try {
            Collection<DTOABMEstadoOrdenCompra> ret = experto.getEstados(soloVigentes);
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/altaEstado")
    public ResponseEntity<?> altaEstado(String nombreEstado) {
        try {
            experto.altaEstado(nombreEstado);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bajaEstado")
    public ResponseEntity<?> bajaEstado(Long idEstadoOrdenCompra) {
        try {
            experto.bajaEstado(idEstadoOrdenCompra);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getDatosEstado")
    public ResponseEntity<?> getDatosEstado(Long idEstadoOrdenCompra) {
        try {
            DTOABMEstadoOrdenCompra ret = experto.getDatosEstado(idEstadoOrdenCompra);
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody DTOABMEstadoOrdenCompra dto) {
        try {
            experto.confirmar(dto);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
