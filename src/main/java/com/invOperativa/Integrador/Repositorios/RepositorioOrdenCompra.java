package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface RepositorioOrdenCompra extends BaseRepository<OrdenCompra, Long>{

    @Query("""
    SELECT DISTINCT oc
    FROM OrdenCompra oc
    JOIN oc.ordenCompraDetalles ocd
    JOIN ocd.articuloProveedor ap
    WHERE ap.proveedor.id = :idProveedor
    """)
    Collection<OrdenCompra> getOrdenesPorProveedor(@Param("idProveedor") Long idProveedor);

    @Query("SELECT oc " +
            "FROM OrdenCompra oc " +
            "WHERE oc.estadoOrdenCompra.id = :idEstadoOrdenCompra")
    Collection<OrdenCompra> getOrdenesPorEstado(@Param("idEstadoOrdenCompra") Long idEstadoOrdenCompra);

    @Query("""
    SELECT o FROM OrdenCompra o
    JOIN o.ordenCompraDetalles d
    JOIN o.estadoOrdenCompra e
    WHERE d.articuloProveedor = :articuloProveedor
    AND e.nombreEstadoOrdenCompra NOT IN ('Finalizada', 'Cancelada')
""")
    Collection<OrdenCompra> findOrdenesNoFinalizadasNiCanceladasByArticuloProveedor(@Param("articuloProveedor") ArticuloProveedor ap);


    @Query("SELECT o " +
            "FROM OrdenCompra o " +
            "WHERE id = :idOrdenCompra " +
            "AND (fhBajaOrdenCompra IS NULL OR fhBajaOrdenCompra > CURRENT_TIMESTAMP)")
    Optional<OrdenCompra> obtenerOCVigentePorID(@Param("idOrdenCompra") Long idOrdenCompra);

}
