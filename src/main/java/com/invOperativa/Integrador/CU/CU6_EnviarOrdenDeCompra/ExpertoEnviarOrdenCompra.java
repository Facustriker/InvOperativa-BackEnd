package com.invOperativa.Integrador.CU.CU6_EnviarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODatosModificacion;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODetallesDatosMod;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODetallesOC;
import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioEstadoOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertoEnviarOrdenCompra {

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    private final RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;

    public DTOEnviarOrdenCompra getDatosOC(Long idOC){
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        if(ordenCompra.isEmpty()){
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        if(!(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Pendiente"))){
            throw new CustomException("Error, la orden de compra seleccionada NO se encuentra en estado pendiente");
        }

        DTOEnviarOrdenCompra dto = DTOEnviarOrdenCompra.builder()
                .idOC(ordenCompra.get().getId())
                .fhAltaOC(ordenCompra.get().getFhAltaOrdenCompra())
                .nombreEstado(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra())
                .build();

        for(OrdenCompraDetalle detalle: ordenCompra.get().getOrdenCompraDetalles()){
            DTODetallesOCEnviar aux = DTODetallesOCEnviar.builder()
                    .cantidad(detalle.getCantidad())
                    .subTotal(detalle.getSubTotal())
                    .costoUnitario(detalle.getArticuloProveedor().getCostoUnitario())
                    .costoPedido(detalle.getArticuloProveedor().getCostoPedido())
                    .isProveedorPredeterminado(detalle.getArticuloProveedor().isPredeterminado())
                    .nombreArt(detalle.getArticuloProveedor().getArticulo().getNombre())
                    .costoAlmacenamientoArt(detalle.getArticuloProveedor().getArticulo().getCostoAlmacenamiento())
                    .nombreProveedor(detalle.getArticuloProveedor().getProveedor().getNombreProveedor())
                    .build();

            dto.addDetalle(aux);
        }

        return dto;
    }

    public void confirmar(Long idOC) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);
        if (ordenCompra.isEmpty()) throw new CustomException("Error, no se encontró la orden de compra seleccionada");

        if (!(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Pendiente"))) throw new CustomException("Error, la orden de compra seleccionada no se encuentra en estado pendiente");

        Optional<EstadoOrdenCompra> estadoEnviada = repositorioEstadoOrdenCompra.obtenerEstadoVigentePorNombre("Enviada");
        if (estadoEnviada.isEmpty()) throw new CustomException("Error, no se encontró el estado 'Enviada' ");
        ordenCompra.get().setEstadoOrdenCompra(estadoEnviada.get());

        repositorioOrdenCompra.save(ordenCompra.get());
    }
}
