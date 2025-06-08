package com.invOperativa.Integrador.CU.CU2_ListarProductosAReponer;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompraDetalle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ExpertoListarProductosAReponer {

//Listado de los artículo que hayan alcanzado el punto de pedido (o estén por debajo) y
//no tengan una orden de compra pendiente o enviada

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    @Autowired
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;


    //Obtener los InventarioArticulos de un Inventario con stock <= puntoPedido
    public Collection<DTOArticulo> getArticulosAReponer() {

        Collection<Articulo> articulos = repositorioArticulo.findAll();
        if (articulos.isEmpty()) {
            throw new CustomException("Error, no existen articulos en el inventario");
        }

        Collection<DTOArticulo> dtoArts = new ArrayList<>();

        for (Articulo art : articulos) {

            int stock = art.getStock();
            int puntoPedido = art.getPuntoPedido();

            if (stock <= puntoPedido && !(repositorioOrdenCompraDetalle.existsArticuloEnOrdenPendienteOEnviada(art.getId()))) {

                DTOArticulo dtoArt = DTOArticulo.builder()
                        .id(art.getId())
                        .costoAlmacenamiento(art.getCostoAlmacenamiento())
                        .nombreArt(art.getNombre())
                        .descripcionArt(art.getDescripcionArt())
                        .fhBajaArticulo(art.getFhBajaArticulo())
                        .inventarioMaxArticulo(art.getInventarioMaxArticulo())
                        .precioUnitario(art.getPrecioUnitario())
                        .proximaRevision(art.getProximaRevision())
                        .puntoPedido(art.getPuntoPedido())
                        .stock(art.getStock())
                        .tiempoFijo(art.getTiempoFijo())
                        .build();

                dtoArts.add(dtoArt);
            }
        }

        return dtoArts;
    }

}
