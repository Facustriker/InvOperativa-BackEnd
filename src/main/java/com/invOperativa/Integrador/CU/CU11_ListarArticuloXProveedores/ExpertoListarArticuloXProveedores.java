package com.invOperativa.Integrador.CU.CU11_ListarArticuloXProveedores;

import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
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

    public Collection<DTOArticuloProv> getArticulosXProv(@RequestParam Long provId) {
        Collection<ArticuloProveedor> articulos = repositorioArticuloProveedor.findByProveedorIdAndFechaBajaIsNull(provId);
        Collection<DTOArticuloProv> dtoArticulos = new ArrayList<DTOArticuloProv>();

        for (ArticuloProveedor ap : articulos) {

            Articulo art = ap.getArticulo();

            DTOArticuloProv aux = DTOArticuloProv.builder()
                    .idArticulo(art.getId())
                    .descripcionArticulo(art.getDescripcionArt())
                    .costoPedido(ap.getCostoPedido())
                    .demoraEntrega(ap.getDemoraEntrega())
                    .costoUnitario(ap.getCostoUnitario())
                    .modeloInventario(ap.getModeloInventario().getNombreModelo())
                    .isPredeterminado(ap.isPredeterminado())
                    .precioUnitario(art.getPrecioUnitario())
                    .build();
            dtoArticulos.add(aux);
        }

        return dtoArticulos;
    }
}
