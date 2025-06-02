package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import com.invOperativa.Integrador.Entidades.Proveedor;
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
                if (!Objects.equals(idProveedorActual, detalleDTO.getIdProveedor())) {
                    Optional<Proveedor> proveedor = repositorioProveedor.getProveedorVigentePorID(detalleDTO.getIdProveedor());
                    proveedor.ifPresent(p -> detalle.getArticuloProveedor().setProveedor(p));
                }
            }
        }

        repositorioOrdenCompra.save(ordenCompra.get());
    }

}
