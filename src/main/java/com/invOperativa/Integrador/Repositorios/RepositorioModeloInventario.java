package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ModeloInventario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RepositorioModeloInventario extends BaseRepository<ModeloInventario, Long>{

    @Query("SELECT m FROM ModeloInventario m WHERE m.id = :id AND m.fhBajaModeloInventario IS NULL")
    Optional<ModeloInventario> findActivoById(@Param("id") Long id);

}
