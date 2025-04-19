package com.invOperativa.Integrador.CU.CU99_CrearArticulo;

import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ExpertoCrearArticulo {

    private final RepositorioArticulo repositorioArticulo;

    public Long crear(DTOCrearArticulo request) throws Exception {

        Articulo articulo = Articulo.builder()
                .nombreArticulo(request.getNombreArticulo())
                .fhaArticulo(new Date())
                .build();

        repositorioArticulo.save(articulo);

        return articulo.getId();
    }
}
