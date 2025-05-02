package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RepositorioArticuloProveedor extends BaseRepository<ArticuloProveedor, Long>{

    @Query("SELECT ap " +
            "FROM ArticuloProveedor ap " +
            "WHERE ap.proveedor.id = :idProveedor")
    Collection<ArticuloProveedor> getArticulosProveedorPorIdProveedor(@Param("idProveedor") Long idProveedor);
}
