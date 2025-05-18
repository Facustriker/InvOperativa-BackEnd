package com.invOperativa.Integrador.CU.CU15_ABMInventario;

import com.invOperativa.Integrador.Entidades.Inventario;
import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioInventario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class ExpertoABMInventario {

    @Autowired
    protected RepositorioInventario repositorioInventario;

    protected RepositorioArticulo repositorioArticulo;

    public Collection<DTOInventario> getAll () {
        Collection<Inventario> inventarios = repositorioInventario.findAll();

        Collection<DTOInventario> dtoInventarios = new ArrayList<>();

        Collection<DTOInventarioArticulo> dtoArticulos = new ArrayList<>();

        for (Inventario inventario: inventarios) {

            Collection<InventarioArticulo> articulos = inventario.getInventarioArticulos();

            for (InventarioArticulo articulo: articulos) {

                DTOInventarioArticulo auxArt = DTOInventarioArticulo.builder()
                        .idInventarioArticulo(articulo.getId())
                        .stock(articulo.getStock())
                        .loteOptimo(articulo.getLoteOptimo())
                        .puntoPedido(articulo.getPuntoPedido())
                        .inventarioMaxArticulo(articulo.getInventarioMaxArticulo())
                        .descripcionArticulo(articulo.getArticulo().getDescripcionArt())
                        .build();

                dtoArticulos.add(auxArt);
            }

            DTOInventario aux = DTOInventario.builder()
                    .idInventario(inventario.getId())
                    .inventarioArticulos(dtoArticulos)
                    .build();
            dtoInventarios.add(aux);
        }

        return dtoInventarios;
    }
}
