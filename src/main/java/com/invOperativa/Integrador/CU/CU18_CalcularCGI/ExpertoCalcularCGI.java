package com.invOperativa.Integrador.CU.CU18_CalcularCGI;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.ModeloInventarioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioModeloInventarioArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoCalcularCGI {

    //Necesito saber el tipo de inventario que usa el proveedor del artículo
    //y ademas de acá saco los artículos
    //Necesito calcular el costo del artículo y el del pedido
    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    private final RepositorioModeloInventarioArticulo repositorioModeloInventarioArticulo;

    public DTOCalcularCGI calculoCGI(Long idArticulo){

        Collection<ArticuloProveedor> artProveedores = repositorioArticuloProveedor.getArticulosProveedorVigentesPorArticuloId(idArticulo);
        Optional<ArticuloProveedor> artProveedorAuxiliar = artProveedores.stream().findFirst();

        if(artProveedorAuxiliar.isEmpty()){
            throw new CustomException("Error, el articulo no tiene un proveedor asignado");
        }

        if ("Lote fijo".equals(artProveedorAuxiliar.get().getModeloInventario().getNombreModelo())) {
            String nombreModelo = artProveedorAuxiliar.get().getModeloInventario().getNombreModelo();
            String nombreArticulo = artProveedorAuxiliar.get().getArticulo().getNombre();

            DTOCalcularCGI dtoCalcularCGI = DTOCalcularCGI.builder()
                    .nombreTipoModelo(nombreModelo)
                    .nombreArticulo(nombreArticulo)
                    .build();

            for(ArticuloProveedor articuloProveedor: artProveedores){
                float costoCompra;
                float costoAlmacenamiento = 0;
                float CGI;

                //Costo pedido
                float costoPedido = articuloProveedor.getCostoPedido();

                //Calculamos costo compra
                float precioUnitario = articuloProveedor.getPrecioUnitario();
                int cantidad = 0;
                List<ModeloInventarioArticulo> modelosInventariosArticulos = repositorioModeloInventarioArticulo.findActivosByArticuloId(idArticulo);
                Optional<ModeloInventarioArticulo> optionalMIA = modelosInventariosArticulos.stream().findFirst();

                if(optionalMIA.isEmpty()){
                    throw new CustomException("Error al obtener un modelo inventario articulo para el articulo seleccionado");
                }

                cantidad = optionalMIA.get().getLoteOptimo();
                costoCompra = precioUnitario * cantidad;

                //Costo almacenamiento
                costoAlmacenamiento = optionalMIA.get().getArticulo().getCostoAlmacenamiento();

                //Calculamos CGI
                CGI = costoCompra + costoPedido + costoAlmacenamiento;


                DTODatosCGI aux = DTODatosCGI.builder()
                        .nombreProveedor(articuloProveedor.getProveedor().getNombreProveedor())
                        .CGI(CGI)
                        .costoCompra(costoCompra)
                        .costoPedido(costoPedido)
                        .costoAlmacenamiento(costoAlmacenamiento)
                        .build();

                dtoCalcularCGI.addDato(aux);
            }

            return dtoCalcularCGI;

        }else{ //El modelo es de tipo Tiempo fijo
            String nombreModelo = artProveedorAuxiliar.get().getModeloInventario().getNombreModelo();
            String nombreArticulo = artProveedorAuxiliar.get().getArticulo().getNombre();

            DTOCalcularCGI dtoCalcularCGI = DTOCalcularCGI.builder()
                    .nombreTipoModelo(nombreModelo)
                    .nombreArticulo(nombreArticulo)
                    .build();

            for(ArticuloProveedor articuloProveedor: artProveedores){
                float costoCompra = 0;
                float costoAlmacenamiento = 0;
                float CGI;

                //Costo pedido
                float costoPedido = articuloProveedor.getCostoPedido();

                //Calculamos costo compra


                //Costo almacenamiento


                //Calculamos CGI
                CGI = costoCompra + costoPedido + costoAlmacenamiento;

                DTODatosCGI aux = DTODatosCGI.builder()
                        .nombreProveedor(articuloProveedor.getProveedor().getNombreProveedor())
                        .CGI(CGI)
                        .costoCompra(costoCompra)
                        .costoPedido(costoPedido)
                        .costoAlmacenamiento(costoAlmacenamiento)
                        .build();

                dtoCalcularCGI.addDato(aux);

            }

            return dtoCalcularCGI;
        }
    }
}
