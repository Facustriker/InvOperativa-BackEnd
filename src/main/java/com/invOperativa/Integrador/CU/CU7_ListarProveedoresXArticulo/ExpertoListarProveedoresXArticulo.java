package com.invOperativa.Integrador.CU.CU7_ListarProveedoresXArticulo;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoListarProveedoresXArticulo {

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    public Collection<DTOProveedor> getProveedoresXArticulo(Long articuloId) {

        //Verifica que exista el articulo con el id ingresado
        Optional<Articulo> articulo = repositorioArticulo.findById(articuloId);
        if(articulo.isEmpty()){
            throw new CustomException("articulo ingresado no existe");
        }

        Collection<ArticuloProveedor> articulosProveedor = repositorioArticuloProveedor.findActivosByArticuloId(articulo.get().getId());

        Collection<DTOProveedor> dtosProv = new ArrayList<>();

        for (ArticuloProveedor artProv : articulosProveedor){

            Proveedor prov = artProv.getProveedor();

            DTOProveedor dtoProveedor = DTOProveedor.builder()
                    .idProveedor(prov.getId())
                    .nombreProveedor(prov.getNombreProveedor())
                    .fhBajaProveedor(prov.getFhBajaProveedor())
                    .build();

            dtosProv.add(dtoProveedor);
        }

        return dtosProv;
    }
}
