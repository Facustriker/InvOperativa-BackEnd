package com.invOperativa.Integrador.CU.CU14_ABMProveedor;

import com.invOperativa.Integrador.Config.CustomException;
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
public class ExpertoABMProveedor {

    @Autowired
    private final RepositorioProveedor repositorioProveedor;

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    public Collection<DTOProveedor> getProveedores(boolean soloVigentes) {
        boolean dadoDeBaja;
        Collection<Proveedor> proveedores;

        if (soloVigentes) {
            proveedores = repositorioProveedor.getProveedoresVigentes();
        } else {
            proveedores = repositorioProveedor.findAll();
        }

        if (proveedores.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<DTOProveedor> dto = new ArrayList<>();

        for (Proveedor proveedor : proveedores) {
            dadoDeBaja = proveedor.getFhBajaProveedor() != null;

            DTOProveedor aux = DTOProveedor.builder()
                    .idProveedor(proveedor.getId())
                    .fhBajaProveedor(proveedor.getFhBajaProveedor())
                    .dadoBaja(dadoDeBaja)
                    .nombreProveedor(proveedor.getNombreProveedor())
                    .build();

            dto.add(aux);
        }

        return dto;
    }

    public void altaProveedor(String nombreProveedor) {

        if (repositorioProveedor.getProveedorVigentePorNombre(nombreProveedor).isPresent()) {
            throw new CustomException("Error, el nombre ingresado ya existe");
        }

        Proveedor proveedor = Proveedor.builder()
                .nombreProveedor(nombreProveedor)
                .build();

        repositorioProveedor.save(proveedor);
    }

    public void darBaja(Long idProveedor) {
        Collection<OrdenCompra> ordenesCompraDeProveedor = repositorioOrdenCompra.getOrdenesPorProveedor(idProveedor);

        for (OrdenCompra oc : ordenesCompraDeProveedor) {
            if (oc.getEstadoOrdenCompra().getFhBajaEstadoOrdenCompra() != null &&
                    (oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Pendiente") ||
                            oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra().equals("Enviada"))) {

                throw new CustomException("Error, no se puede dar de baja al proveedor porque tiene una orden de compra asociada en estado "
                        + oc.getEstadoOrdenCompra().getNombreEstadoOrdenCompra());
            }
        }

        Collection<ArticuloProveedor> articulosProveedor = repositorioArticuloProveedor.getArticulosProveedorPorIdProveedor(idProveedor);

        for (ArticuloProveedor ap : articulosProveedor) {
            if (ap.isPredeterminado()) {
                throw new CustomException("Error, no se puede dar de baja al proveedor porque es un proveedor predeterminado en el artículo " +
                        ap.getArticulo().getId() + " con descripción: " + ap.getArticulo().getDescripcionArt());
            }
        }

        Optional<Proveedor> proveedor = repositorioProveedor.findById(idProveedor);

        if (proveedor.isEmpty()) {
            throw new CustomException("Error, el proveedor no existe");
        }

        proveedor.get().setFhBajaProveedor(new Date());
        repositorioProveedor.save(proveedor.get());
    }

    public DTOProveedor getDatosProveedor(Long idProveedor) {
        Optional<Proveedor> proveedor = repositorioProveedor.findById(idProveedor);

        if (proveedor.isEmpty()) {
            throw new CustomException("Error, el proveedor seleccionado no existe");
        }

        return DTOProveedor.builder()
                .idProveedor(proveedor.get().getId())
                .nombreProveedor(proveedor.get().getNombreProveedor())
                .build();
    }

    public void confirmar(DTOProveedor dto) {
        Optional<Proveedor> proveedor = repositorioProveedor.findById(dto.getIdProveedor());

        if (proveedor.isEmpty()) {
            throw new CustomException("Error, el proveedor seleccionado no existe");
        }

        if (repositorioProveedor.getProveedorVigentePorNombre(dto.getNombreProveedor()).isPresent()) {
            throw new CustomException("Error, el nombre ingresado ya se encuentra en uso");
        }

        proveedor.get().setNombreProveedor(dto.getNombreProveedor());
        repositorioProveedor.save(proveedor.get());
    }
}
