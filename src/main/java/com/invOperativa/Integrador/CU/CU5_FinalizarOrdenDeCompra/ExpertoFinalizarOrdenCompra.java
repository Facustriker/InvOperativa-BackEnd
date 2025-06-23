package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import com.invOperativa.Integrador.Entidades.ModeloInventario;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioEstadoOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioModeloInventario;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoFinalizarOrdenCompra {

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    private final RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    @Autowired
    private final RepositorioModeloInventario repositorioModeloInventario;




    // El usuario me ingresa un id de orden de compra y yole muestro los datos y todos sus detallesOrdenCompra
    public DTOFinalizarOrdenCompra getDatosOC(Long idOC){
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        if(ordenCompra.isEmpty()){
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        if(!(ordenCompra.get().getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Enviada"))){
            throw new CustomException("Error, la orden de compra seleccionada NO se encuentra en estado Enviada");
        }

        DTOFinalizarOrdenCompra dto = DTOFinalizarOrdenCompra.builder()
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
                    .stock(detalle.getArticuloProveedor().getArticulo().getStock())
                    .puntoPedido(detalle.getArticuloProveedor().getArticulo().getPuntoPedido())
                    .build();

            dto.getDetallesOC().add(aux);
        }

        return dto;
    }

    // Me entra un DTO en el que me dice si el usuario quiere finalizar la orden de compra o no.

    @Transactional
    public DTORespuestaFinalizarOC confirmar(Long idOC) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);
        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }

        // Obtener el estado "Finalizada"
        EstadoOrdenCompra estadoFinalizada = repositorioEstadoOrdenCompra.findByNombreEstadoOrdenCompraAndFhBajaEstadoOrdenCompraIsNull("Finalizada");
        if (estadoFinalizada == null) {
            throw new CustomException("Error, no se encontró el estado Finalizada");
        }

        // Buscar el modelo de tiempo fijo
        ModeloInventario modeloTiempoFijo = repositorioModeloInventario.getModelosVigentes().stream()
                .filter(m -> m.getNombreModelo().equals("Tiempo Fijo"))
                .findFirst()
                .orElseThrow(() -> new CustomException("Error, no se encontró el modelo de tiempo fijo"));

        // Actualizar el estado de la orden
        ordenCompra.get().setEstadoOrdenCompra(estadoFinalizada);

        // Actualizar el inventario y verificar punto de pedido
        boolean requiereAtencion = false;
        DTORespuestaFinalizarOC respuesta = DTORespuestaFinalizarOC.builder()
            .requiereAtencion(false)
            .build();

        for (OrdenCompraDetalle detalle : ordenCompra.get().getOrdenCompraDetalles()) {
            Articulo articulo = detalle.getArticuloProveedor().getArticulo();
            
            // Actualizar stock
            int nuevoStock = articulo.getStock() + detalle.getCantidad();
            articulo.setStock(nuevoStock);
            repositorioArticulo.save(articulo);

            // Verificar si NO es modelo Tiempo fijo y si no supera el punto de pedido
            if (detalle.getArticuloProveedor().getModeloInventario().getId() != modeloTiempoFijo.getId()
                && articulo.getPuntoPedido() != null 
                && nuevoStock <= articulo.getPuntoPedido()) {
                requiereAtencion = true;
                
                DTODetalleAtencion detalleAtencion = DTODetalleAtencion.builder()
                    .nombreArticulo(articulo.getNombre())
                    .stockActual(nuevoStock)
                    .puntoPedido(articulo.getPuntoPedido())
                    .costoUnitario(detalle.getArticuloProveedor().getCostoUnitario())
                    .costoPedido(detalle.getArticuloProveedor().getCostoPedido())
                    .nombreProveedor(detalle.getArticuloProveedor().getProveedor().getNombreProveedor())
                    .build();
                
                respuesta.getDetallesAtencion().add(detalleAtencion);
            }
        }

        // Guardar la orden actualizada
        repositorioOrdenCompra.save(ordenCompra.get());

        //Si no se superó el punto de pedido, mando un bool avisando
        respuesta.setRequiereAtencion(requiereAtencion);
        return respuesta;
    }
}
