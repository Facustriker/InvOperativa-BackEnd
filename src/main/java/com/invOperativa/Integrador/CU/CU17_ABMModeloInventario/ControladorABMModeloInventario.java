package com.invOperativa.Integrador.CU.CU17_ABMModeloInventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ABMModeloInventario")
public class ControladorABMModeloInventario {

    @Autowired
    protected ExpertoABMModeloInventario experto;

    @GetMapping("/getModelos")
    public ResponseEntity<?> getModelos(@RequestParam boolean soloVigentes) {
        Collection<DTOABMModeloInventario> ret = experto.getModelos(soloVigentes);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/altaModelo")
    public ResponseEntity<?> altaModelo(@RequestParam String nombreModelo) {
        experto.altaModelo(nombreModelo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bajaModelo")
    public ResponseEntity<?> bajaModelo(@RequestParam Long idModeloInventario) {
        experto.bajaModelo(idModeloInventario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getDatosModelo")
    public ResponseEntity<?> getDatosModelo(@RequestParam Long idModeloInventario) {
        DTOABMModeloInventario ret = experto.getDatosModelo(idModeloInventario);
        return ResponseEntity.ok(ret);
    }

    @PutMapping("/modificarModelo")
    public ResponseEntity<?> modificarModelo(@RequestBody DTOABMModeloInventario dto) {
        experto.modificarModelo(dto);
        return ResponseEntity.ok().build();
    }
}
