package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RepositorioInventarioArticulo extends BaseRepository<InventarioArticulo, Long>{
}
