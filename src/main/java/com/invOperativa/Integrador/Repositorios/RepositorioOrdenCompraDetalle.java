package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.OrdenCompraDetalle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
