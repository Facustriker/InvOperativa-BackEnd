package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Entidades.ModeloInventarioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioModeloInventarioArticulo;
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
    private final RepositorioArticulo repositorio;

    @Autowired
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    private final RepositorioModeloInventarioArticulo repositorioModeloInventarioArticulo;

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
                .fhBajaArticulo(null)
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

        List<ArticuloProveedor> articuloProveedores = repositorioArticuloProveedor.findActivosByArticuloId(id);

        if (!articuloProveedores.isEmpty()){
            for (ArticuloProveedor relacion : articuloProveedores) {
                relacion.setFechaBaja(new Date());
                repositorioArticuloProveedor.save(relacion);
            }
        }

        List<ModeloInventarioArticulo> modeloInventarioArticuloList = repositorioModeloInventarioArticulo.findActivosByArticuloId(id);

        if (!modeloInventarioArticuloList.isEmpty()){
            for (ModeloInventarioArticulo modelo : modeloInventarioArticuloList) {
                modelo.setFechaBaja(new Date());
                repositorioModeloInventarioArticulo.save(modelo);
            }
        }

        artExistente.setFhBajaArticulo(new Date());

        repositorio.save(artExistente);
    }

    // Trae los articulos según como se desee, solo vigentes o todos
    public List<Articulo> traerTodos(boolean soloVigentes) {
        return soloVigentes ? repositorio.findByfhBajaArticuloIsNull() : repositorio.findAll();
    }

}
