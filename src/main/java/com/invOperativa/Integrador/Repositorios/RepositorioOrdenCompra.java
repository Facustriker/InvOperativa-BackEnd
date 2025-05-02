package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.OrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RepositorioOrdenCompra extends BaseRepository<OrdenCompra, Long>{

    @Query("SELECT oc " +
            "FROM OrdenCompra oc " +
            "WHERE oc.proveedor.id = :idProveedor")
    Collection<OrdenCompra> getOrdenesPorProveedor(@Param("idProveedor") Long idProveedor);
}
