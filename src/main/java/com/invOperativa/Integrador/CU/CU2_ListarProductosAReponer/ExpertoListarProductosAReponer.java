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
    private final RepositorioArticuloProveedor repositoriArticuloProveedor;

    @Autowired
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;


    //Obtener los InventarioArticulos de un Inventario con stock <= puntoPedido
    public Collection<DTOArticulo> getArticulosAReponer() {

        Collection<Articulo> articulos = repositorioArticulo.findByfhBajaArticuloIsNull();
        if (articulos.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<DTOArticulo> dtoArts = new ArrayList<>();

        for (Articulo art : articulos) {

            int stock = art.getStock();


            //Obtener el articuloProveedor que sea predeterminado
            Collection<ArticuloProveedor> articulosProveedor = repositoriArticuloProveedor.getArticulosProveedorVigentesPorArticuloId(art.getId());
            
            //Me fijo si el articulo tiene articulosProveedor relacionados
            if (articulosProveedor.isEmpty()) {
                continue;
            }

            //Si tiene articulosProveedor relacionados, me fijo cuál es el predeterminado
            ArticuloProveedor articuloProveedorPredeterminado = null;

            for (ArticuloProveedor ap : articulosProveedor){ 
                if (ap.isPredeterminado()) {
                    articuloProveedorPredeterminado = ap;
                }
            }
            
            //Si no tiene articuloProveedor predeterminado, no se puede calcular el stockSeguridad
            if (articuloProveedorPredeterminado == null) {
                System.out.println("Advertencia: El artículo " + art.getNombre() + " no tiene un proveedor predeterminado, por favor asignelo para poder obtener el stock de Seguridad");
                continue;
            }

            if (articuloProveedorPredeterminado.getModeloInventario().getNombreModelo().equals("Tiempo fijo")){
                continue;
            }
            
            //Obtener el stockSeguridad
            int stockSeguridad = articuloProveedorPredeterminado.getStockSeguridad();

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
                        .stockSeguridad(stockSeguridad)
                        .build();

                dtoArts.add(dtoArt);
            }
        }

        return dtoArts;
    }

}
