package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ModeloInventarioArticulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RepositorioModeloInventarioArticulo extends BaseRepository<ModeloInventarioArticulo, Long>{

    @Query("SELECT mia FROM ModeloInventarioArticulo mia " +
            "WHERE mia.articulo.id = :idArticulo " +
            "AND mia.fechaBaja IS NULL " +
            "AND mia.articulo.fhBajaArticulo IS NULL")
    List<ModeloInventarioArticulo> findActivosByArticuloId(@Param("idArticulo") Long idArticulo);

}
