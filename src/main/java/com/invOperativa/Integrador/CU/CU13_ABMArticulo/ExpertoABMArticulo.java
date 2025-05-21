package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioInventarioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompraDetalle;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertoABMArticulo {

    @Autowired
    private RepositorioArticulo repositorio;

    @Autowired
    private RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    @Autowired
    private RepositorioInventarioArticulo repositorioInventarioArticulo;

    // Da de alta el articulo verificando los valores
    @Transactional
    public void altaArticulo(Articulo art){

        if (art.getPrecioUnitario() <= 0) {
            throw new CustomException("El precio unitario no puede ser menor o igual a 0");
        }

        if (art.getDescripcionArt().trim().isEmpty()){
            throw new CustomException("La descripción del artículo no puede estar vacía");
        }

        if (art.getNombre().trim().isEmpty()){
            throw new CustomException("El nombre del artículo no puede estar vacío");
        }

        if (art.getCostoAlmacenamiento() < 0){
            throw new CustomException("El costo de almacenamiento no puede ser negativo");
        }

        Articulo articulo = Articulo.builder()
                .costoAlmacenamiento(art.getCostoAlmacenamiento())
                .descripcionArt(art.getDescripcionArt())
                .precioUnitario(art.getPrecioUnitario())
                .fechaBaja(null)
                .nombre(art.getNombre())
                .build();

        repositorio.save(articulo);
    }

    // Permite modificar un articulo que ya existe
    @Transactional
    public void modificarArticulo(Articulo art) {

        Articulo artExistente = repositorio.findById(art.getId()).orElseThrow(() -> new CustomException("No existe el articulo que desea modificar") );

        if (art.getPrecioUnitario() <= 0) {
            throw new CustomException("El precio unitario no puede ser menor o igual a 0");
        }

        if (art.getDescripcionArt().trim().isEmpty()){
            throw new CustomException("La descripción del artículo no puede estar vacía");
        }

        if (art.getNombre().trim().isEmpty()){
            throw new CustomException("El nombre del artículo no puede estar vacío");
        }

        if (art.getCostoAlmacenamiento() < 0){
            throw new CustomException("El costo de almacenamiento no puede ser negativo");
        }

        artExistente.setDescripcionArt(art.getDescripcionArt());
        artExistente.setNombre(art.getNombre());
        artExistente.setCostoAlmacenamiento(art.getCostoAlmacenamiento());
        artExistente.setPrecioUnitario(art.getPrecioUnitario());

        repositorio.save(artExistente);

    }

    // Coloca fechaBaja en un articulo verificando antes sus relaciones
    @Transactional
    public void bajarArticulo(Long id) {
        Articulo artExistente = repositorio.findById(id).orElseThrow(() -> new CustomException("No existe el articulo que desea modificar") );

        if (repositorioOrdenCompraDetalle.existsArticuloEnOrdenPendienteOEnviada(id)) {
            throw new CustomException("El artículo está presente en una orden pendiente o enviada y no puede darse de baja.");
        }

        List<InventarioArticulo> inventarioArticulos = repositorioInventarioArticulo.findByArticuloIdAndFechaBajaIsNull(id);

        // Ponemos fecha de baja a los inventario articulos
        if (!inventarioArticulos.isEmpty()){
            for (InventarioArticulo inventarioArticulo: inventarioArticulos){
                inventarioArticulo.setFechaBaja(new Date());
                repositorioInventarioArticulo.save(inventarioArticulo);
            }
        }

        artExistente.setFechaBaja(new Date());

        repositorio.save(artExistente);
    }
}
