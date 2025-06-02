package com.invOperativa.Integrador.CU.CU7_ListarProveedoresXArticulo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path= "/ListarProveedoresXArticulo")
public class ControladorListarProveedoresXArticulo {

    @Autowired
    protected ExpertoListarProveedoresXArticulo experto;

    @GetMapping("/getProveedoresXArticulo")
    public ResponseEntity<Collection<DTOProveedor>> getProveedoresXArticulo(@RequestParam Long articuloId){
        Collection<DTOProveedor> listaDto = experto.getProveedoresXArticulo(articuloId);
        return ResponseEntity.ok(listaDto);
    }
}
