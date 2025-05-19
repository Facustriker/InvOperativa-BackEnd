package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RepositorioInventarioArticulo extends BaseRepository<InventarioArticulo, Long>{

    //Obtener todos los InventarioArticulo de un Inventario
    // PARA CAMBIAR --> 1. inventarioIdFk (Por el que tenga el ABM Inventario)
    @Query("SELECT * " +
            "FROM InventarioArticulo as invArt" +
            "WHERE invArt.inventarioIdFk = inventarioId")
    Collection<InventarioArticulo> getInventarioArticulos(@Param("inventarioId") Long id);

}
