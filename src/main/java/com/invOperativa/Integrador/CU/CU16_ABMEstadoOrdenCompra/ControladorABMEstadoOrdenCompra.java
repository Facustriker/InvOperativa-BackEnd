package com.invOperativa.Integrador.CU.CU16_ABMEstadoOrdenCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/ABMEstadoOrdenCompra")
public class ControladorABMEstadoOrdenCompra {

    @Autowired
    protected ExpertoABMEstadoOrdenCompra experto;

    @GetMapping("/getEstados")
    public ResponseEntity<?> getEstados(@RequestParam boolean soloVigentes) {
        Collection<DTOABMEstadoOrdenCompra> ret = experto.getEstados(soloVigentes);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/altaEstado")
    public ResponseEntity<?> altaEstado(@RequestParam String nombreEstado) {
        experto.altaEstado(nombreEstado);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bajaEstado")
    public ResponseEntity<?> bajaEstado(@RequestParam Long idEstadoOrdenCompra) {
        experto.bajaEstado(idEstadoOrdenCompra);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getDatosEstado")
    public ResponseEntity<?> getDatosEstado(@RequestParam Long idEstadoOrdenCompra) {
        DTOABMEstadoOrdenCompra ret = experto.getDatosEstado(idEstadoOrdenCompra);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody DTOABMEstadoOrdenCompra dto) {
        experto.confirmar(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificar(@RequestBody DTOModificarEstado dto){
        experto.modificar(dto);
        return ResponseEntity.ok().build();
    }

}
