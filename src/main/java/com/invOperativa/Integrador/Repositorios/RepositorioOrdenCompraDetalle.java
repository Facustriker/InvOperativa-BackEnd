package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioOrdenCompraDetalle extends BaseRepository<OrdenCompraDetalle, Long>{

    // Consulta para saber si un artículo está en una orden de compra en estado pendiente o enviada
        @Query("""
        SELECT COUNT(ocd) > 0
        FROM OrdenCompraDetalle ocd
        JOIN ocd.articuloProveedor ap
        JOIN ap.articulo a
        JOIN OrdenCompra oc ON ocd MEMBER OF oc.ordenCompraDetalles
        JOIN oc.estadoOrdenCompra eoc
        WHERE a.id = :articuloId
        AND eoc.nombreEstadoOrdenCompra IN ('Pendiente', 'Enviada')
    """)
        boolean existsArticuloEnOrdenPendienteOEnviada(@Param("articuloId") Long articuloId);

        @Query("""
        SELECT ocd FROM OrdenCompraDetalle ocd
        JOIN ocd.articuloProveedor ap
        JOIN OrdenCompra oc ON ocd MEMBER OF oc.ordenCompraDetalles
        JOIN oc.estadoOrdenCompra eoc
        WHERE ap.id = :id
        AND (eoc.nombreEstadoOrdenCompra = 'Pendiente' OR eoc.nombreEstadoOrdenCompra = 'Enviada')
    """)
        List<OrdenCompraDetalle> findByArticuloProveedorEnOrdenesPendientesOEnviadas(@Param("id") Long id);

            @Query("""
            SELECT SUM(d.cantidad) 
            FROM OrdenCompraDetalle d 
            JOIN d.articuloProveedor ap 
            JOIN ap.articulo a 
            JOIN OrdenCompra oc ON d MEMBER OF oc.ordenCompraDetalles
            WHERE a.id = :articuloId 
              AND oc.estadoOrdenCompra.nombreEstadoOrdenCompra = 'Enviado'
              AND oc.fhBajaOrdenCompra IS NULL
        """)
            Integer obtenerCantidadTotalDeArticuloEnviado(@Param("articuloId") Long articuloId);


}
