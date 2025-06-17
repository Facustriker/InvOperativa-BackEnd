package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    public void confirmar(DTODatosModificacion dto) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(dto.getIdOC());
        if (ordenCompra.isEmpty()) throw new CustomException("Error, no se encontró la orden de compra seleccionada");

        Map<Long, DTODetallesDatosMod> detallesMap = dto.getDetallesMod().stream()
                .collect(Collectors.toMap(DTODetallesDatosMod::getIdOCDetalle, Function.identity()));

        for (OrdenCompraDetalle detalle : ordenCompra.get().getOrdenCompraDetalles()) {
            DTODetallesDatosMod detalleDTO = detallesMap.get(detalle.getId());
            if (detalleDTO != null) {
                detalle.setCantidad(detalleDTO.getCantidad());

                Long idProveedorActual = detalle.getArticuloProveedor().getProveedor().getId();
                Long idProveedorNuevo = detalleDTO.getIdProveedor();

                if (Objects.equals(idProveedorActual, idProveedorNuevo)) {
                    throw new CustomException("El proveedor seleccionado para el artículo '" + detalle.getArticuloProveedor().getArticulo().getNombre() + "' es el mismo que el actual.");
                }

                Optional<Proveedor> proveedor = repositorioProveedor.getProveedorVigentePorID(idProveedorNuevo);
                proveedor.ifPresent(p -> detalle.getArticuloProveedor().setProveedor(p));
            }
        }

        repositorioOrdenCompra.save(ordenCompra.get());
    }

}
