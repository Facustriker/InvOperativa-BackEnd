package com.invOperativa.Integrador.CU.CU2_ListarProductosAReponer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path= "/ListarProductosAReponer")
public class ControladorListarProductosAReponer {

    @Autowired
    protected ExpertoListarProductosAReponer experto;

    @GetMapping("/getArticulosAReponer")
    public ResponseEntity<Collection<DTOArticulo>> getArticulos(){
        Collection<DTOArticulo> listaDto = experto.getArticulosAReponer();
        return ResponseEntity.ok(listaDto);
    }

}
