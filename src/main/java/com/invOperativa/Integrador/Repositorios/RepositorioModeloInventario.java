package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ModeloInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.Collection;

public interface RepositorioModeloInventario extends BaseRepository<ModeloInventario, Long>{

    @Query("SELECT m FROM ModeloInventario m WHERE m.id = :id AND m.fhBajaModeloInventario IS NULL")
    Optional<ModeloInventario> findActivoById(@Param("id") Long id);

    @Query("SELECT m FROM ModeloInventario m WHERE m.fhBajaModeloInventario IS NULL")
    Collection<ModeloInventario> getModelosVigentes();

}
