package com.invOperativa.Integrador.CU.CU2_ListarProductosAReponer;

import com.invOperativa.Integrador.CU.CU14_ABMProveedor.DTOProveedor;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioInventarioArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertoListarProductosAReponer {

//Listado de los artículo que hayan alcanzado el punto de pedido (o estén por debajo) y
//no tengan una orden de compra pendiente o enviada

    @Autowired
    private final RepositorioInventarioArticulo repositorioInventarioArticulo;

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    //Obtener los InventarioArticulos de un Inventario con stock <= puntoPedido
    public Collection<DTOInventarioArticulo> getInventarioArticulosAReponer(Long inventarioId) {

        Collection<InventarioArticulo> inventarioArticulos;

        //Cambiar cuando ya esté el ABMArticulo
        Long placeholder_id = 1L;

        inventarioArticulos = repositorioInventarioArticulo.getInventarioArticulos(placeholder_id);

        if (inventarioArticulos.isEmpty()) {
            throw new CustomException("Error, el inventario no tiene articulos asignados");
        }

        Collection<DTOInventarioArticulo> dtoInvArts = new ArrayList<>();

        for (InventarioArticulo invArt : inventarioArticulos) {

            int stock = invArt.getStock();
            int puntoPedido = invArt.getPuntoPedido();

            if (stock <= puntoPedido) {

                //Leer el Articulo del InventarioArticulo y crea DTOInvArticulo
                Articulo art = invArt.getArticulo();

                DTOInventarioArticulo dtoInvArt = DTOInventarioArticulo.builder()
                        .idInvArt(invArt.getId())
                        .inventarioMaxArticulo(invArt.getInventarioMaxArticulo())
                        .loteOptimo(invArt.getLoteOptimo())
                        .puntoPedido(invArt.getPuntoPedido())
                        .stock(invArt.getStock())
                        .stockSeguridad(invArt.getStockSeguridad())
                        .idArt(art.getId())
                        .descripcionArt(art.getDescripcionArt())
                        .precioUnitario(art.getPrecioUnitario())
                        .build();

                dtoInvArts.add(dtoInvArt);
            }
        }

        return dtoInvArts;
    }

}
