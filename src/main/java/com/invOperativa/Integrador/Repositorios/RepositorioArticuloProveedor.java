package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;

public interface RepositorioArticuloProveedor extends BaseRepository<ArticuloProveedor, Long>{

    @Query("SELECT ap " +
            "FROM ArticuloProveedor ap " +
            "WHERE ap.proveedor.id = :idProveedor")
    Collection<ArticuloProveedor> getArticulosProveedorPorIdProveedor(@Param("idProveedor") Long idProveedor);

    @Query("SELECT ap FROM ArticuloProveedor ap " +
            "WHERE ap.articulo.id = :idArticulo " +
            "AND ap.fechaBaja IS NULL " +
            "AND ap.articulo.fhBajaArticulo IS NULL")
    List<ArticuloProveedor> findActivosByArticuloId(@Param("idArticulo") Long idArticulo);


    //Agrego esto para el ABM de ModeloInventario (para evitar dar de baja un modelo que tiene artículos asociados)
    @Query("SELECT ap " +
            "FROM ArticuloProveedor ap " +
            "WHERE ap.modeloInventario.id = :idModeloInventario")
    Collection<ArticuloProveedor> getAPPorModeloInventario(@Param("idModeloInventario") Long idModeloInventario);
}

