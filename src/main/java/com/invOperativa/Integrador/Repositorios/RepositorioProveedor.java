package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface RepositorioProveedor extends BaseRepository<Proveedor, Long>{

    @Query("SELECT p " +
            "FROM Proveedor p " +
            "WHERE (fhBajaProveedor IS NULL OR fhBajaProveedor > CURRENT_TIMESTAMP) ")
    Collection<Proveedor> getProveedoresVigentes();
}
