package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Venta;

import java.util.List;

public interface RepositorioVenta extends BaseRepository<Venta, Long>{

    // Metodo derivado: ordena por fhAltaVenta descendente
    List<Venta> findAllByOrderByFhAltaVentaDesc();

}
