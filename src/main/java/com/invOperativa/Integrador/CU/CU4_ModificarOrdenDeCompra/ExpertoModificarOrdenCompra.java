package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.DTODetalleOrden;
import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.DTONuevaOrden;
import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.ExpertoGenerarOrdenDeCompra;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioProveedor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertoModificarOrdenCompra {

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    private final RepositorioProveedor repositorioProveedor;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    public ExpertoGenerarOrdenDeCompra expertoGenerarOrdenDeCompra;

    public DTOModificarOrdenCompra getDatosOC(Long idOC){
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        if(ordenCompra.isEmpty()){
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        if(!(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Pendiente"))){
            throw new CustomException("Error, la orden de compra seleccionada NO se encuentra en estado pendiente");
        }

        DTOModificarOrdenCompra dto = DTOModificarOrdenCompra.builder()
                .idOC(ordenCompra.get().getId())
                .fhAltaOC(ordenCompra.get().getFhAltaOrdenCompra())
                .build();

        for(OrdenCompraDetalle detalle: ordenCompra.get().getOrdenCompraDetalles()){
            DTODetallesOC aux = DTODetallesOC.builder()
                    .idOCDetalle(detalle.getId())
                    .cantidad(detalle.getCantidad())
                    .subTotal(detalle.getSubTotal())
                    .costoUnitario(detalle.getArticuloProveedor().getCostoUnitario())
                    .costoPedido(detalle.getArticuloProveedor().getCostoPedido())
                    .nombreArt(detalle.getArticuloProveedor().getArticulo().getNombre())
                    .costoAlmacenamientoArt(detalle.getArticuloProveedor().getArticulo().getCostoAlmacenamiento())
                    .nombreProveedor(detalle.getArticuloProveedor().getProveedor().getNombreProveedor())
                    .isPredeterminado(detalle.getArticuloProveedor().isPredeterminado())
                    .puntoPedido(detalle.getArticuloProveedor().getArticulo().getPuntoPedido())
                    .stock(detalle.getArticuloProveedor().getArticulo().getStock())
                    .build();

            dto.addDetalle(aux);
        }

        return dto;
    }

    @Transactional
    public void confirmar(DTODatosModificacion dto) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(dto.getIdOC());
        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        // Map para acceso rápido a detalles modificados
        Map<Long, DTODetallesDatosMod> detallesMap = dto.getDetallesMod().stream()
                .collect(Collectors.toMap(DTODetallesDatosMod::getIdOCDetalle, Function.identity()));

        // Validación punto de pedido
        for (OrdenCompraDetalle detalle : ordenCompra.get().getOrdenCompraDetalles()) {
            DTODetallesDatosMod detalleDTO = detallesMap.get(detalle.getId());
            if (detalleDTO != null) {
                int puntoPedido = detalle.getArticuloProveedor().getArticulo().getPuntoPedido();
                if (detalleDTO.getCantidad() < puntoPedido && !dto.isConfirmadoPorUsuario()) {
                    throw new CustomException(
                            "La cantidad para el artículo '" + detalle.getArticuloProveedor().getArticulo().getNombre() +
                                    "' es menor al punto de pedido (" + puntoPedido + "). Si quieres continuar, confirma nuevamente la operación."
                    );
                }
            }
        }

        // Solo actualizamos la cantidad de cada detalle
        for (OrdenCompraDetalle detalle : ordenCompra.get().getOrdenCompraDetalles()) {
            DTODetallesDatosMod detalleDTO = detallesMap.get(detalle.getId());
            if (detalleDTO != null) {
                detalle.setCantidad(detalleDTO.getCantidad());
            }
        }

        // Guardamos la orden modificada
        repositorioOrdenCompra.save(ordenCompra.get());
    }


    @Transactional
    public void eliminarDetalle(Long idOc, Long idOCDetalle) {
        Optional<OrdenCompra> ordenCompraOpt = repositorioOrdenCompra.obtenerOCVigentePorID(idOc);
        if (ordenCompraOpt.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        OrdenCompra ordenCompra = ordenCompraOpt.get();

        // Buscar el detalle correspondiente
        OrdenCompraDetalle detalleAEliminar = ordenCompra.getOrdenCompraDetalles().stream()
                .filter(d -> d.getId().equals(idOCDetalle))
                .findFirst()
                .orElseThrow(() -> new CustomException("Error, no existe un detalle con id " + idOCDetalle + " para esta orden de compra."));

        // Eliminar el detalle de la colección
        ordenCompra.getOrdenCompraDetalles().remove(detalleAEliminar);

        // Guardar la orden de compra actualizada
        repositorioOrdenCompra.save(ordenCompra);
    }


}
