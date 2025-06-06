package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.*;
import com.invOperativa.Integrador.Repositorios.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpertoGenerarOrdenDeCompra {

    private final RepositorioArticulo repositorioArticulo;
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;
    private final RepositorioOrdenCompra repositorioOrdenCompra;
    private final RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;
    private final RepositorioProveedor repositorioProveedor;
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    @Transactional
    public void nuevaOrden(DTONuevaOrden dto){

        List<DTODetalleOrden> detalles = dto.getDetalles();

        Long proveedorId = detalles.get(0).getArticuloProveedorId();

        Proveedor proveedor = repositorioProveedor.findActivoById(proveedorId).orElseThrow(()-> new CustomException("No existe un proveedor activo con ese id"));

        float totalAuxiliar = 0;

        List<OrdenCompraDetalle> ordenCompraDetalles = new ArrayList<>();

        for (DTODetalleOrden detalle : detalles){

            if (!detalle.getArticuloProveedorId().equals(proveedorId)){
                throw new CustomException("Uno de los artículos es de un proveedor distinto");
            }

            if (detalle.getCantidad() <= 0) {
                throw new CustomException("La cantidad a pedir debe ser mayor a 0");
            }

            ArticuloProveedor articuloProveedor = repositorioArticuloProveedor.findById(detalle.getArticuloProveedorId()).orElseThrow(()-> new CustomException("No existe uno de los artículo-proveedor que busca"));

            float subTotal = detalle.getCantidad() * articuloProveedor.getArticulo().getPrecioUnitario();

            if (Math.abs(subTotal - detalle.getSubTotal()) > 0.01f){
                throw new CustomException("Uno de los subtotales está mal calculado");
            }

            totalAuxiliar += detalle.getSubTotal();

            OrdenCompraDetalle ordenCompraDetalle = OrdenCompraDetalle.builder()
                    .articuloProveedor(articuloProveedor)
                    .cantidad(detalle.getCantidad())
                    .subTotal(detalle.getSubTotal())
                    .build();

            repositorioOrdenCompraDetalle.save(ordenCompraDetalle);

        }

        if (Math.abs(totalAuxiliar - dto.getTotal()) > 0.01f){
            throw new CustomException("El total está mal calculado");
        }

        OrdenCompra ordenCompra = OrdenCompra.builder()
                .proveedor(proveedor)
                .estadoOrdenCompra(buscarEstadoPendiente())
                .fhBajaOrdenCompra(null)
                .ordenCompraDetalles(ordenCompraDetalles)
                .fhAltaOrdenCompra(new Date())
                .isAuto(false)
                .total(dto.getTotal())
                .build();

        repositorioOrdenCompra.save(ordenCompra);

   }

    @Transactional
    public void generacionAutomatica(List<Long> articuloIds) {

        if (articuloIds == null || articuloIds.isEmpty()) {
            return;
        }

        List<Articulo> articulos = repositorioArticulo.findAllById(articuloIds);

        if (articulos.isEmpty()) {
            throw new CustomException("No se encontraron artículos válidos para generar la orden de compra");
        }

        //    Para cada Artículo, buscamos su ArticuloProveedor predeterminado.
        //    Si un Artículo no tiene ArticuloProveedor predeterminado, lo ignoramos (o podríamos lanzar excepción).
        //    Al mismo tiempo, vamos armando un Map<Proveedor, List<ArticuloProveedor>>.

        Map<Proveedor, List<ArticuloProveedor>> agrupadosPorProveedor = new HashMap<>();

        for (Articulo articulo : articulos) {

            ArticuloProveedor apPredeterminado =
                    repositorioArticuloProveedor
                            .findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(articulo.getId())
                            .orElse(null);

            if (apPredeterminado == null) {
                throw new CustomException("Uno de los articulos no tiene proveedor predeterminado");
            }

            Proveedor prov = apPredeterminado.getProveedor();
            if (prov == null) {
                throw new CustomException("El articulo-proveedor no tiene un proveedor válido");
            }

            // Agregamos apPredeterminado (que une Articulo + Proveedor) al Map
            agrupadosPorProveedor
                    .computeIfAbsent(prov, k -> new ArrayList<>())
                    .add(apPredeterminado);
        }

        // 3) Ahora recorremos cada entrada del Map y generamos una OrdenCompra única por proveedor.
        for (Map.Entry<Proveedor, List<ArticuloProveedor>> entrada : agrupadosPorProveedor.entrySet()) {

            Proveedor proveedor = entrada.getKey();
            List<ArticuloProveedor> listaAP = entrada.getValue();

            OrdenCompra oc = OrdenCompra.builder()
                    .estadoOrdenCompra(buscarEstadoPendiente())
                    .fhAltaOrdenCompra(new Date())
                    .isAuto(true)
                    .proveedor(proveedor)
                    .build();


            for (ArticuloProveedor ap : listaAP) {

                OrdenCompraDetalle detalleOC = new OrdenCompraDetalle();

                if (ap.getLoteOptimo() != null && ap.getLoteOptimo() > 0) {
                    detalleOC.setCantidad(ap.getLoteOptimo());
                } else {
                    throw new CustomException("El lote óptimo de un artículo es nulo");
                }

                float costoUnitario = ap.getCostoUnitario();
                float cantidad = detalleOC.getCantidad();
                detalleOC.setSubTotal(costoUnitario * cantidad);

                detalleOC.setArticuloProveedor(ap);

                oc.addOrdenCompraDetalle(detalleOC);
            }

            // Persistimos la OrdenCompra completa (gracias a Cascade.ALL, también persiste los detalles)
            repositorioOrdenCompra.save(oc);
        }
    }

    //Metodo auxiliar para buscar en la base de datos el objeto EstadoOrdenCompra
    private EstadoOrdenCompra buscarEstadoPendiente() {
        return repositorioEstadoOrdenCompra
                .findByNombreEstadoOrdenCompra("Pendiente")
                .orElseThrow(() -> new CustomException("No existe estado 'Pendiente' en EstadoOrdenCompra"));
    }
}
