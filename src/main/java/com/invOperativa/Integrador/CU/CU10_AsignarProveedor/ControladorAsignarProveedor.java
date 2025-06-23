package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        List<ArticuloProveedor> ap = experto.getAll();
        return ResponseEntity.ok(ap);
    }

    @PostMapping("/modificar")
    public ResponseEntity<?> modificar (@RequestBody DTOAsignarProveedor dto){
        experto.modificarAsignacion(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        experto.eliminarAsignacion(id);
        return ResponseEntity.ok().build();
    }

}
