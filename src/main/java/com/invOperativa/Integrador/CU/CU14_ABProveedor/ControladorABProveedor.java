package com.invOperativa.Integrador.CU.CU14_ABProveedor;

import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ABProveedor")
public class ControladorABProveedor {

    @Autowired
    protected ExpertoABProveedor experto;

    @GetMapping("/getProveedores")
    public ResponseEntity<?> getProveedores() {
        try {
            Collection<DTOProveedor> ret = experto.getProveedores();
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/alta")
    public ResponseEntity<?> altaProveedor() {
        try {
            experto.altaProveedor();
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/darBaja")
    public ResponseEntity<?> darBaja(Long idProveedor) {
        try {
            experto.darBaja(idProveedor);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
