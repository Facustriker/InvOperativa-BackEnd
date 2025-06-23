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

        //Traigo el resto de proveedores que pueden ser seleccionados
        Collection<Proveedor> proveedoresDisponibles = repositorioProveedor.getProveedoresVigentes();
        if(proveedoresDisponibles.isEmpty()){
            throw new CustomException("Error, no hay proveedores");
        }

        for(Proveedor prov: proveedoresDisponibles){
            Collection<ArticuloProveedor> artProveedorDispo = repositorioArticuloProveedor.getArticulosProveedorVigentesPorIdProveedor(prov.getId());
            Optional<ArticuloProveedor> artAux = artProveedorDispo.stream().findFirst();
            if(artAux.isEmpty()){
                throw new CustomException("Error, no hay primer proveedor");
            }
            DTOProveedor auxProveedores = DTOProveedor.builder()
                    .idProveedor(artAux.get().getProveedor().getId())
                    .nombreProveedor(artAux.get().getProveedor().getNombreProveedor())
                    .costoPedido(artAux.get().getCostoPedido())
                    .costoUnitario(artAux.get().getCostoUnitario())
                    .build();

            dto.addProveedor(auxProveedores);
        }

        return dto;
    }

    @Transactional
    public void confirmar(DTODatosModificacion dto) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(dto.getIdOC());
        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        // 1️⃣ MAP de detalles para acceder rápidamente
        Map<Long, DTODetallesDatosMod> detallesMap = dto.getDetallesMod().stream()
                .collect(Collectors.toMap(DTODetallesDatosMod::getIdOCDetalle, Function.identity()));

        // 2️⃣ Verificación de punto de pedido
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

        // 3️⃣ LISTAS para organizar detalles cambiados
        List<DTODetalleOrden> detallesNuevosPorProveedor = new ArrayList<>();

        // 4️⃣ MAP para verificar que no queden duplicados de artículo por proveedor
        Map<Long, Long> articuloProveedorMap = new HashMap<>();

        // 5️⃣ ACTUALIZAMOS detalles actuales y preparamos detalles para nueva Orden
        for (OrdenCompraDetalle detalle : ordenCompra.get().getOrdenCompraDetalles()) {
            DTODetallesDatosMod detalleDTO = detallesMap.get(detalle.getId());
            if (detalleDTO == null) {
                continue;
            }

            Long idProveedorOriginal = detalle.getArticuloProveedor().getProveedor().getId();
            Long idProveedorNuevo = detalleDTO.getIdProveedor();

            // Si NO CAMBIA DE PROVEEDOR, actualizamos cantidad directamente
            if (Objects.equals(idProveedorOriginal, idProveedorNuevo)) {
                detalle.setCantidad(detalleDTO.getCantidad());
            } else {
                // CAMBIA DE PROVEEDOR --> Preparamos para crear una nueva Orden
                ArticuloProveedor apNuevo = repositorioArticuloProveedor.findByIdAndFechaBajaIsNull(detalleDTO.getIdProveedor())
                        .orElseThrow(() -> new CustomException("Error: No existe el proveedor seleccionado para el artículo '"
                                + detalle.getArticuloProveedor().getArticulo().getNombre() + "'"));

                detallesNuevosPorProveedor.add(
                        DTODetalleOrden.builder()
                                .cantidad(detalleDTO.getCantidad())
                                .subTotal(detalleDTO.getCantidad() * apNuevo.getCostoUnitario())
                                .articuloProveedorId(apNuevo.getId())
                                .build()
                );

                // Se elimina de esta Orden para no mezclar proveedores
                detalle.setCantidad(0);
            }

            Long idArticulo = detalle.getArticuloProveedor().getArticulo().getId();

            articuloProveedorMap.put(idArticulo, detalle.getArticuloProveedor().getProveedor().getId());
        }

        // 6️⃣ GUARDAMOS la Orden de Compra actual
        repositorioOrdenCompra.save(ordenCompra.get());

        // 7️⃣ GENERAMOS nuevas Órdenes de Compra para detalles cambiados de proveedor
        if (!detallesNuevosPorProveedor.isEmpty()) {
            DTONuevaOrden nuevaOrden = DTONuevaOrden.builder()
                    .detalles(detallesNuevosPorProveedor)
                    .confirmacion(true) // Se confirma para que no genere error por pendientes
                    .build();

            expertoGenerarOrdenDeCompra.nuevaOrden(nuevaOrden);
        }
    }


}
