package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/GenerarOrdenCompra")
public class ControladorGenerarOrdenDeCompra {

    @Autowired
    public ExpertoGenerarOrdenDeCompra experto;

    // Lleva todos los articulos existentes que no estén dados de baja
    @GetMapping("/traerArticulos")
    public ResponseEntity<?> getAll(){
        List<Articulo> articulos = experto.traerTodos();
        return ResponseEntity.ok(articulos);
    }

    // Devuelve todos los proveedores de un artículo y te da cuanto comprar para el predeterminado
    @GetMapping("/sugerirOrden")
    public ResponseEntity<?> get(@RequestParam Long idArticulo){
        DTOSugerirOrdenDetalle sugerencia = experto.sugerirOrden(idArticulo);
        return ResponseEntity.ok(sugerencia);
    }

    @PostMapping("/nuevaOrden")
    public ResponseEntity<?> post(@RequestBody DTONuevaOrden dto){
        DTOSalidaOrdenDeCompra dtoSalidaOrdenDeCompra = experto.nuevaOrden(dto);
        return ResponseEntity.ok(dtoSalidaOrdenDeCompra);
    }

    @GetMapping("/traerOrdenesVigentes")
    public ResponseEntity<?> traerOrdenes(){
        List<OrdenCompra> ordenes = experto.traerOrdenes();
        return ResponseEntity.ok(ordenes);
    }
}
