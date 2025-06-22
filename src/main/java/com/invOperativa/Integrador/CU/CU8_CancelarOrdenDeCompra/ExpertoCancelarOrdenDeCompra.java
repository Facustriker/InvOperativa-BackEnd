package com.invOperativa.Integrador.CU.CU8_CancelarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.DTOSalidaOrdenDeCompra;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioEstadoOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoCancelarOrdenDeCompra {

    @Autowired
    RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;

    public DTOEstadoOC getEstadoOC(Long idOC) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        DTOEstadoOC dtoEstadoOc = DTOEstadoOC.builder()
                .nombreEstado(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra())
                .build();

        return dtoEstadoOc;
    }

    public void cancelarOC(Long idOC) {

        //Ubicar la orden de compra que desea cancelar
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        //Ubicar el estado "Cancelada"
        Optional<EstadoOrdenCompra> estadoCancelada = repositorioEstadoOrdenCompra.obtenerEstadoVigentePorNombre("Cancelada");

        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }
        if (estadoCancelada.isEmpty()) {
            throw new CustomException("Error, no se encontró el estado 'Cancelada'");
        }

        //Cambiar estado a "Cancelada"
        ordenCompra.get().setEstadoOrdenCompra(estadoCancelada.get());

        repositorioOrdenCompra.save(ordenCompra.get());
    }

}
