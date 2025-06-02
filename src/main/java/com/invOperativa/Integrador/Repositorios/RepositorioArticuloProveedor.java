package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT ap FROM ArticuloProveedor ap " +
            "WHERE ap.proveedor.id = :idProveedor " +
            "AND ap.articulo.proximaRevision <= CURRENT_DATE " +
            "AND ap.isPredeterminado = 1 " +
            "AND ap.modeloInventario.nombreModelo = 'Tiempo Fijo'")
    Collection<ArticuloProveedor> findTiempoFijo(@Param("idProveedor") Long idProveedor);
}
