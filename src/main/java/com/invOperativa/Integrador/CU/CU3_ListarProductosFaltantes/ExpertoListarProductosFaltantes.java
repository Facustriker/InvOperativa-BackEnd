package com.invOperativa.Integrador.CU.CU3_ListarProductosFaltantes;

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
public class ExpertoListarProductosFaltantes {

//Listado de los artículo que estén en el punto del stock d seguridad o por debajo y
//no tengan una orden de compra pendiente o enviada

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    @Autowired
    private final RepositorioArticuloProveedor repositoriArticuloProveedor;

    @Autowired
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;


    //Obtener los InventarioArticulos de un Inventario con stock <= stockSeguridad
    public Collection<DTOArticulo> getArticulosFaltantes() {
        
        //Obtener todos los articulos
        Collection<Articulo> articulos = repositorioArticulo.findAll();
        if (articulos.isEmpty()) {
            throw new CustomException("Error, no existen articulos en el inventario");
        }

        Collection<DTOArticulo> dtoArts = new ArrayList<>();


        //Recorrer todos los articulos
        for (Articulo art : articulos) {

            //Obtener el stock del articulo
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
            
            //Obtener el stockSeguridad
            int stockSeguridad = articuloProveedorPredeterminado.getStockSeguridad();

            //Si el stock es menor o igual al stockSeguridad y no tiene una orden de compra pendiente o enviada, se agrega al listado
            if (stock <= stockSeguridad && !(repositorioOrdenCompraDetalle.existsArticuloEnOrdenPendienteOEnviada(art.getId()))) {

                DTOArticulo dtoArt = DTOArticulo.builder()
                        .id(art.getId())
                        .nombreArt(art.getNombre())
                        .stock(art.getStock())
                        .stockSeguridad(stockSeguridad)
                        .costoAlmacenamiento(art.getCostoAlmacenamiento())
                        .descripcionArt(art.getDescripcionArt())
                        .fhBajaArticulo(art.getFhBajaArticulo())
                        .inventarioMaxArticulo(art.getInventarioMaxArticulo())
                        .precioUnitario(art.getPrecioUnitario())
                        .proximaRevision(art.getProximaRevision())
                        .puntoPedido(art.getPuntoPedido())
                        .build();

                dtoArts.add(dtoArt);
            }
        }

        return dtoArts;
    }

}
