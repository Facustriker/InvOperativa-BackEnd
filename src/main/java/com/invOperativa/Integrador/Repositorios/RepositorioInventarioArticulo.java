package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.InventarioArticulo;

import java.util.List;

public interface RepositorioInventarioArticulo extends BaseRepository<InventarioArticulo, Long>{

    // Consulta que devuelve los inventarioArticulo con un articulo específico
    List<InventarioArticulo> findByArticuloIdAndFechaBajaIsNull(Long articuloId);
}
