package com.invOperativa.Integrador.CU.CU14_ABProveedor;

import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Entidades.Proveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoABProveedor {

    @Autowired
    private final RepositorioProveedor repositorioProveedor;

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    public Collection<DTOProveedor> getProveedores(boolean soloVigentes) throws Exception{
        boolean dadoDeBaja;
        Collection<Proveedor> proveedores;

        if(soloVigentes){
            proveedores = repositorioProveedor.getProveedoresVigentes();
        }else{
            proveedores = repositorioProveedor.findAll();
        }

        if(proveedores.isEmpty()){
            throw new Exception("Error, no se han encontrado proveedores");
        }

        Collection<DTOProveedor> dto = new ArrayList<>();

        for(Proveedor proveedor: proveedores){
            if(proveedor.getFhBajaProveedor() == null){
                dadoDeBaja = false;
            }else{
                dadoDeBaja = true;
            }

            DTOProveedor aux = DTOProveedor.builder()
                    .idProveedor(proveedor.getId())
                    .fhBajaProveedor(proveedor.getFhBajaProveedor())
                    .dadoBaja(dadoDeBaja)
                    .build();

            dto.add(aux);
        }

        return dto;
    }


    public void altaProveedor(){

        Proveedor proveedor = Proveedor.builder()
                .build();

        repositorioProveedor.save(proveedor);
    }


    public void darBaja(Long idProveedor) throws Exception{

        Collection<OrdenCompra> ordenesCompraDeProveedor = repositorioOrdenCompra.getOrdenesPorProveedor(idProveedor);

        for(OrdenCompra oc: ordenesCompraDeProveedor){
            if (oc.getEstadoOrdenCompra().getFhBajaEstadoOrdenCompra() != null &&
                    (oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Pendiente") ||
                            oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Enviada"))) {

                throw new Exception("Error, no se puede dar de baja al proveedor porque tiene una orden de compra asociada en estado "
                        + oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra());
            }
        }

        Collection<ArticuloProveedor> articulosProveedor = repositorioArticuloProveedor.getArticulosProveedorPorIdProveedor(idProveedor);

        for(ArticuloProveedor ap: articulosProveedor){
            if(ap.isPredeterminado()){
                throw new Exception("Error, no se puede dar de baja al proveedor porque es un proveedor predeterminado en el articulo "+ap.getArticulo().getId()+ "con descripcion: "+ap.getArticulo().getDescripcionArt());
            }
        }

        Optional<Proveedor> proveedor = repositorioProveedor.findById(idProveedor);

        proveedor.get().setFhBajaProveedor(new Date());

        repositorioProveedor.save(proveedor.get());

    }

}
