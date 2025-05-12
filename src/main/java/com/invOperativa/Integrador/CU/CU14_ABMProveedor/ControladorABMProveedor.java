package com.invOperativa.Integrador.CU.CU14_ABMProveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ABMProveedor")
public class ControladorABMProveedor {

    @Autowired
    protected ExpertoABMProveedor experto;

    @GetMapping("/getProveedores")
    public ResponseEntity<Collection<DTOProveedor>> getProveedores(@RequestParam boolean soloVigentes) {
        Collection<DTOProveedor> ret = experto.getProveedores(soloVigentes);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/alta")
    public ResponseEntity<Void> altaProveedor(@RequestParam String nombreProveedor) {
        experto.altaProveedor(nombreProveedor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/darBaja")
    public ResponseEntity<Void> darBaja(@RequestParam Long idProveedor) {
        experto.darBaja(idProveedor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getDatosProveedor")
    public ResponseEntity<DTOProveedor> getDatosProveedor(@RequestParam Long idProveedor) {
        DTOProveedor ret = experto.getDatosProveedor(idProveedor);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmar(@RequestBody DTOProveedor dto) {
        experto.confirmar(dto);
        return ResponseEntity.ok().build();
    }
}
