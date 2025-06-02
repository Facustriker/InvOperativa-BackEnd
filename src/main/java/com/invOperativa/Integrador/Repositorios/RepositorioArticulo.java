package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepositorioArticulo extends BaseRepository<Articulo, Long>{

    // Trae los articulo que no estén dados de baja
    List<Articulo> findByfhBajaArticuloIsNull();

    @Query("SELECT a FROM Articulo a WHERE a.id = :id AND a.fhBajaArticulo IS NULL")
    Optional<Articulo> findActivoById(@Param("id") Long id);


}
