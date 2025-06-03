package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import com.invOperativa.Integrador.CU.CU10_AsignarProveedor.ExpertoAsignarProveedor;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompraDetalle;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoABMArticulo {

    @Autowired
    private final RepositorioArticulo repositorio;

    @Autowired
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    // Metodo auxiliar para revisar articulos que llegan
    public void revisarArticulo(Articulo art){

        if (art.getCostoAlmacenamiento() < 0){
            throw new CustomException("El costo de almacenamiento no puede ser menor a 0");
        }

        if (art.getDemanda() < 0){
            throw new CustomException("La demanda no puede ser menor a 0");
        }

        if (art.getDescripcionArt().trim().isEmpty()){
            throw new CustomException("La descripción del artículo no puede estar vacía");
        }

        if (art.getInventarioMaxArticulo() < 0){
            throw new CustomException("El inventario maximo no puede ser menor a 0");
        }

        if (art.getNombre().trim().isEmpty()){
            throw new CustomException("El nombre del artículo no puede estar vacío");
        }

        if (art.getPrecioUnitario() <= 0) {
            throw new CustomException("El precio unitario no puede ser menor o igual a 0");
        }

        if (art.getStock() < 0) {
            throw new CustomException("El stock no puede ser menor a 0");
        }

    }

    // Metodo auxiliar para revisar articulos que llegan
    public void revisarArticuloDTO(DTOArticulo art){

        if (art.getCostoAlmacenamiento() < 0){
            throw new CustomException("El costo de almacenamiento no puede ser menor a 0");
        }

        if (art.getDemanda() < 0){
            throw new CustomException("La demanda no puede ser menor a 0");
        }

        if (art.getDescripcionArt().trim().isEmpty()){
            throw new CustomException("La descripción del artículo no puede estar vacía");
        }

        if (art.getInventarioMaxArticulo() < 0){
            throw new CustomException("El inventario maximo no puede ser menor a 0");
        }

        if (art.getNombre().trim().isEmpty()){
            throw new CustomException("El nombre del artículo no puede estar vacío");
        }

        if (art.getPrecioUnitario() <= 0) {
            throw new CustomException("El precio unitario no puede ser menor o igual a 0");
        }

        if (art.getStock() < 0) {
            throw new CustomException("El stock no puede ser menor a 0");
        }

    }

    // Da de alta el articulo verificando los valores
    @Transactional
    public void altaArticulo(DTOArticulo art){

        revisarArticuloDTO(art);

        Articulo articulo = Articulo.builder()
                .costoAlmacenamiento(art.getCostoAlmacenamiento())
                .demanda(art.getDemanda())
                .descripcionArt(art.getDescripcionArt())
                .fhBajaArticulo(null)
                .inventarioMaxArticulo(art.getInventarioMaxArticulo())
                .nombre(art.getNombre())
                .precioUnitario(art.getPrecioUnitario())
                .proximaRevision(null)
                .puntoPedido(null)
                .stock(art.getStock())
                .tiempoFijo(null)
                .build();

        repositorio.save(articulo);
    }

    // Permite modificar un articulo que ya existe
    @Transactional
    public void modificarArticulo(Articulo art) {

        Articulo artExistente = repositorio.findById(art.getId()).orElseThrow(() -> new CustomException("No existe el articulo que desea modificar") );

        revisarArticulo(art);

        if (art.getDemanda() != artExistente.getDemanda()){

            ArticuloProveedor proveedorPredeterminado = repositorioArticuloProveedor.findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(artExistente.getId()).orElseThrow(()->new CustomException("No hay proveedor predeterminado"));

            int demanda = art.getDemanda();
            float tiempoEntrega = proveedorPredeterminado.getDemoraEntrega();
            float nivelServicio = proveedorPredeterminado.getNivelServicio();
            double z = ArticuloProveedor.getZ(nivelServicio);
            double desviacion = 0.25F * Math.sqrt(tiempoEntrega);

            int puntoPedido = (int) Math.round((demanda / 365.0) * tiempoEntrega + z * desviacion);

            artExistente.setPuntoPedido(puntoPedido);

        }

        artExistente.setCostoAlmacenamiento(art.getCostoAlmacenamiento());
        artExistente.setDescripcionArt(art.getDescripcionArt());
        artExistente.setInventarioMaxArticulo(art.getInventarioMaxArticulo());
        artExistente.setPrecioUnitario(art.getPrecioUnitario());
        artExistente.setStock(art.getStock());
        artExistente.setNombre(art.getNombre());
        artExistente.setDemanda(art.getDemanda());

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

        artExistente.setFhBajaArticulo(new Date());

        repositorio.save(artExistente);
    }

    // Trae los articulos según como se desee, solo vigentes o todos
    public List<Articulo> traerTodos(boolean soloVigentes) {
        return soloVigentes ? repositorio.findByfhBajaArticuloIsNull() : repositorio.findAll();
    }

}
