package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface RepositorioArticulo extends BaseRepository<Articulo, Long>{

    @Query("SELECT ar" +
            "FROM Articulo AS ar" +
            "WHERE ar.id = :articuloId")
    Articulo getArticulo(@RequestParam("articuloId") Long articuloId);
}
