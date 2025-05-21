package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Articulo;
import java.util.List;

public interface RepositorioArticulo extends BaseRepository<Articulo, Long>{

    // Trae los articulo que no estén dados de baja
    List<Articulo> findByFechaBajaIsNull();

}
