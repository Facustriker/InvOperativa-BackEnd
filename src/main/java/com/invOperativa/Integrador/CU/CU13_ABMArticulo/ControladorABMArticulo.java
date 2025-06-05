package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ABMArticulo")
public class ControladorABMArticulo {

    @Autowired
    private ExpertoABMArticulo experto;

    // Dar de alta nuevo articulo
    @PostMapping("/alta")
    public ResponseEntity<?> alta(@RequestParam DTOArticulo art) {
        experto.altaArticulo(art);
        return ResponseEntity.ok().build();
    }

    // Modificar artículo existente
    @PutMapping("/modificar")
    public ResponseEntity<?> modificar(@RequestParam Articulo art){
        experto.modificarArticulo(art);
        return ResponseEntity.ok().build();
    }

    // Baja lógica de un artículo
    @PutMapping("/baja/{id}")
    public ResponseEntity<?> baja(@PathVariable Long id){
        experto.bajarArticulo(id);
        return ResponseEntity.ok().build();
    }

    // Lleva todos los articulos existentes
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestParam boolean soloVigentes){
        List<Articulo> articulos = experto.traerTodos(soloVigentes);
        return ResponseEntity.ok(articulos);
    }

}
