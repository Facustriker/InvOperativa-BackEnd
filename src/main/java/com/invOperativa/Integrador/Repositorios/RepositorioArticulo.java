package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

public interface RepositorioArticulo extends BaseRepository<Articulo, Long>{

    @Query("SELECT ar" +
            " FROM Articulo ar " +
            "WHERE ar.id = :articuloId")
    Articulo getArticulo(@RequestParam("articuloId") Long articuloId);

    // Trae los articulo que no estén dados de baja
    List<Articulo> findByfhBajaArticuloIsNull();

}
