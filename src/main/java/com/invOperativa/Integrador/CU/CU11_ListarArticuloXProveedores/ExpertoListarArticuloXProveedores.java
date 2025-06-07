package com.invOperativa.Integrador.CU.CU11_ListarArticuloXProveedores;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class ExpertoListarArticuloXProveedores {

    @Autowired
    private RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    private RepositorioProveedor repositorioProveedor;

    //obtener una lista de todos los proveedores
    public Collection<DTOProveedor> getProveedores(){

        Collection<DTOProveedor> dtosProveedores = new ArrayList<>();
        Collection<Proveedor> proveedores = repositorioProveedor.findAll();

        for(Proveedor prov : proveedores){
            DTOProveedor dtoProv = DTOProveedor.builder()
                    .idProv(prov.getId())
                    .nombreProveedor(prov.getNombreProveedor())
                    .fhBajaProveedor(prov.getFhBajaProveedor())
                    .build();

            dtosProveedores.add(dtoProv);
        }
        return dtosProveedores;
    }

    public Collection<DTOArticuloProv> getArticulosXProv(@RequestParam Long provId) {
        Collection<ArticuloProveedor> articulos = repositorioArticuloProveedor.findByProveedorIdAndFechaBajaIsNull(provId);
        Collection<DTOArticuloProv> dtoArticulos = new ArrayList<DTOArticuloProv>();

        for (ArticuloProveedor ap : articulos) {

            Articulo art = ap.getArticulo();

            DTOArticuloProv aux = DTOArticuloProv.builder()
                    .idArticulo(art.getId())
                    .nombreArticulo(art.getNombre())
                    .descripcionArticulo(art.getDescripcionArt())
                    .costoPedido(ap.getCostoPedido())
                    .demoraEntrega(ap.getDemoraEntrega())
                    .costoUnitario(ap.getCostoUnitario())
                    .modeloInventario(ap.getModeloInventario().getNombreModelo())
                    .isPredeterminado(ap.isPredeterminado())
                    .precioUnitario(art.getPrecioUnitario())
                    .stock(art.getStock())
                    .build();
            dtoArticulos.add(aux);
        }

        if (dtoArticulos.isEmpty()) {
            throw new CustomException("Error, no se han encontrado articulos para este proveedor");
        }

        return dtoArticulos;
    }
}
