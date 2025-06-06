package com.invOperativa.Integrador.CU.CU11_ListarArticuloXProveedores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ListarArtículoXProveedor")
public class ControladorListarArticuloXProveedores {

    @Autowired
    private ExpertoListarArticuloXProveedores experto;

    @GetMapping(path="/GetProveedores")
    public ResponseEntity<?> getProveedores() {
        Collection<DTOProveedor> req = experto.getProveedores();
        return ResponseEntity.ok(req);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> getArticulosXProv(@RequestParam Long provId) {
        Collection<DTOArticuloProv> req = experto.getArticulosXProv(provId);
        return ResponseEntity.ok(req);
    }
}


