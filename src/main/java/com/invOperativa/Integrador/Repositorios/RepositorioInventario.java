package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Inventario;
import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface RepositorioInventario extends BaseRepository<Inventario, Long>{
}
