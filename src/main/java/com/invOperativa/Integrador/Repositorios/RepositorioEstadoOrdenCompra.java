package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface RepositorioEstadoOrdenCompra extends BaseRepository<EstadoOrdenCompra, Long>{

    @Query("SELECT e " +
            "FROM EstadoOrdenCompra e " +
            "WHERE (fhBajaEstadoOrdenCompra IS NULL OR fhBajaEstadoOrdenCompra > CURRENT_TIMESTAMP) ")
    Collection<EstadoOrdenCompra> getEstadosVigentes();

    @Query("SELECT e " +
            "FROM EstadoOrdenCompra e " +
            "WHERE id = :idEstadoOrdenCompra " +
            "AND (fhBajaEstadoOrdenCompra IS NULL OR fhBajaEstadoOrdenCompra > CURRENT_TIMESTAMP)")
    Optional<EstadoOrdenCompra> obtenerEstadoVigentePorID(@Param("idEstadoOrdenCompra") Long idEstadoOrdenCompra);

    EstadoOrdenCompra findByNombreEstadoOrdenCompraAndFhBajaEstadoOrdenCompraIsNull(String nombreEstadoOrdenCompra);

    Optional<EstadoOrdenCompra> findByNombreEstadoOrdenCompra(String nombre);
}
