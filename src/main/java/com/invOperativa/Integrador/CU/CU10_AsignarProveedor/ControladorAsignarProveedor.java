package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asignarProveedor")
public class ControladorAsignarProveedor {

    @Autowired
    private ExpertoAsignarProveedor experto;

    @PostMapping("/asignar")
    public ResponseEntity<?> asignar (@RequestBody DTOAsignarProveedor dto){
        experto.asignarProveedor(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modificar")
    public ResponseEntity<?> modificar (@RequestParam DTOAsignarProveedor dto){
        experto.modificarAsignacion(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        experto.eliminarAsignacion(id);
        return ResponseEntity.ok().build();
    }

}
