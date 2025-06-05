package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface RepositorioProveedor extends BaseRepository<Proveedor, Long>{

    @Query("SELECT p " +
            "FROM Proveedor p " +
            "WHERE (fhBajaProveedor IS NULL OR fhBajaProveedor > CURRENT_TIMESTAMP) ")
    Collection<Proveedor> getProveedoresVigentes();

    @Query("SELECT p " +
            "FROM Proveedor p " +
            "WHERE p.nombreProveedor = :nombreProveedor " +
            "AND (fhBajaProveedor IS NULL OR fhBajaProveedor > CURRENT_TIMESTAMP)")
    Optional<Proveedor> getProveedorVigentePorNombre(@Param("nombreProveedor") String nombreProveedor);

    @Query("SELECT p " +
            "FROM Proveedor p " +
            "WHERE p.id = :idProveedor " +
            "AND (fhBajaProveedor IS NULL OR fhBajaProveedor > CURRENT_TIMESTAMP)")
    Optional<Proveedor> getProveedorVigentePorID(@Param("idProveedor") Long idProveedor);

    @Query("SELECT p FROM Proveedor p WHERE p.id = :id AND p.fhBajaProveedor IS NULL")
    Optional<Proveedor> findActivoById(@Param("id") Long id);

}
