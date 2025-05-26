package com.invOperativa.Integrador.Repositorios;

import com.invOperativa.Integrador.Entidades.ModeloInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface RepositorioModeloInventario extends JpaRepository<ModeloInventario, Long> {
    @Query("SELECT m FROM ModeloInventario m WHERE m.fhBajaModeloInventario IS NULL")
    Collection<ModeloInventario> getModelosVigentes();
}
