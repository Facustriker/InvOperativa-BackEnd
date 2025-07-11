package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RepositorioArticulo extends BaseRepository<Articulo, Long>{

    @Query("SELECT ar" +
            " FROM Articulo ar " +
            "WHERE ar.id = :articuloId")
    Articulo getArticulo(@RequestParam("articuloId") Long articuloId);

    // Trae los articulo que no estén dados de baja
    List<Articulo> findByfhBajaArticuloIsNull();

    @Query("SELECT a " +
            "FROM Articulo a " +
            "WHERE id = :idArticulo " +
            "AND (fhBajaArticulo IS NULL OR fhBajaArticulo > CURRENT_TIMESTAMP)")
    Optional<Articulo> getArticuloVigentePorId(@RequestParam("idArticulo") Long idArticulo);

    @Query("SELECT a FROM Articulo a WHERE a.id = :id AND a.fhBajaArticulo IS NULL")
    Optional<Articulo> findActivoById(@Param("id") Long id);




}
