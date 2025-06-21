package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
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

    @Query("SELECT ap " +
            "FROM ArticuloProveedor ap " +
            "WHERE ap.proveedor.id = :idProveedor " +
            "AND (fechaBaja IS NULL OR fechaBaja > CURRENT_TIMESTAMP)")
    Collection<ArticuloProveedor> getArticulosProveedorVigentesPorIdProveedor(@Param("idProveedor") Long idProveedor);

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
    
    @Query("SELECT ap FROM ArticuloProveedor ap " +
            "WHERE ap.articulo.id = :idArticulo " +
            "AND ap.fechaBaja IS NULL " +
            "AND ap.articulo.fhBajaArticulo IS NULL")
    Collection<ArticuloProveedor> getArticulosProveedorVigentesPorArticuloId(@Param("idArticulo") Long idArticulo);

    // Metodo que te devuelve el ArticuloProveedor predeterminado para un artículo
    Optional<ArticuloProveedor> findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(Long articuloId);

    @Query("SELECT ap FROM ArticuloProveedor ap " +
                        "WHERE ap.proveedor.id = :idProveedor " +
                        "AND FUNCTION('DATE', ap.articulo.proximaRevision) <= CURRENT_DATE " +
                        "AND ap.isPredeterminado = True " +
                        "AND ap.modeloInventario.nombreModelo = 'Tiempo Fijo'")
    Collection<ArticuloProveedor> findTiempoFijo(@Param("idProveedor") Long idProveedor);

    //Devuelve ArticuloProveedor relacionados a un proveedor
    Collection<ArticuloProveedor> findByProveedorIdAndFechaBajaIsNull(Long proveedorId);

    // Devuelve el articulo proveedor activo según el id
    Optional<ArticuloProveedor> findByIdAndFechaBajaIsNull(Long id);
}

