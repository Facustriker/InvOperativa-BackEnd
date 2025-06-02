package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

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
}
