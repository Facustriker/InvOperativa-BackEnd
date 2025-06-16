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
    private final RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    // Busca ordenes de compra pendientes para ese mismo articulo
    public List<Long> buscarPendientes(List<Long> articulosIds){

        return repositorioOrdenCompra.findArticuloIdsEnOrdenesPendientesOEnviadas(articulosIds);

    }

    @Transactional
    public DTOSalidaOrdenDeCompra nuevaOrden(DTONuevaOrden dto) {

        List<DTODetalleOrden> detalles = dto.getDetalles();

        if (detalles == null || detalles.isEmpty()) {
            throw new CustomException("La orden no contiene detalles");
        }

        // 1. Verificar pendientes
        List<Long> articuloIds = detalles.stream()
                .map(DTODetalleOrden::getArticuloProveedorId)
                .map(id -> repositorioArticuloProveedor.findByIdAndFechaBajaIsNull(id)
                        .orElseThrow(() -> new CustomException("No existe un artículo-proveedor activo con id " + id)))
                .map(ap -> ap.getArticulo().getId())
                .toList();

        List<Long> pendientesIds = buscarPendientes(articuloIds);

        if (!pendientesIds.isEmpty() && !dto.isConfirmacion()) {
            List<String> nombres = pendientesIds.stream()
                    .map(id -> repositorioArticulo.findById(id)
                            .map(Articulo::getNombre)
                            .orElse("Artículo desconocido (ID: " + id + ")"))
                    .toList();
            return DTOSalidaOrdenDeCompra.builder()
                    .nombresPedidos(nombres)
                    .build();
        }

        // 2. Agrupar detalles por proveedor
        Map<Proveedor, List<DTODetalleOrden>> detallesPorProveedor = new HashMap<>();

        for (DTODetalleOrden dtoDet : detalles) {

            ArticuloProveedor ap = repositorioArticuloProveedor
                    .findByIdAndFechaBajaIsNull(dtoDet.getArticuloProveedorId())
                    .orElseThrow(() -> new CustomException(
                            "No existe un artículo-proveedor activo con id " + dtoDet.getArticuloProveedorId()));

            Proveedor prov = ap.getProveedor();

            if (prov == null) {
                throw new CustomException("El artículo-proveedor " + ap.getId() + " no tiene proveedor asociado");
            }

            detallesPorProveedor
                    .computeIfAbsent(prov, k -> new ArrayList<>())
                    .add(dtoDet);
        }

        // 3. Para cada proveedor, crear y guardar una orden
        List<Long> ordenesCreadas = new ArrayList<>();
        EstadoOrdenCompra estadoPendiente = buscarEstadoPendiente();
        Date ahora = new Date();

        DTOSalidaOrdenDeCompra dtoSalidaOrdenDeCompra = DTOSalidaOrdenDeCompra.builder().nombresPedidos(new ArrayList<>()).build();

        for (Map.Entry<Proveedor, List<DTODetalleOrden>> entry : detallesPorProveedor.entrySet()) {

            Proveedor proveedor = entry.getKey();

            List<DTODetalleOrden> dtos = entry.getValue();

            // Construcción de la orden
            OrdenCompra oc = OrdenCompra.builder()
                    .estadoOrdenCompra(estadoPendiente)
                    .fhAltaOrdenCompra(ahora)
                    .isAuto(false)                // marcamos que es manual
                    .proveedor(proveedor)
                    .build();

            float total = 0;

            List<DTODetalleOrdenCompra> dtoDetalles = new ArrayList<>();

            // Detalles de la orden
            for (DTODetalleOrden dtoDet : dtos) {
                ArticuloProveedor ap = repositorioArticuloProveedor
                        .findByIdAndFechaBajaIsNull(dtoDet.getArticuloProveedorId())
                        .get();  // ya existía porque agrupamos antes

                OrdenCompraDetalle det = new OrdenCompraDetalle();
                det.setCantidad(dtoDet.getCantidad());
                det.setSubTotal(dtoDet.getSubTotal());
                det.setArticuloProveedor(ap);
                oc.addOrdenCompraDetalle(det);

                total += dtoDet.getSubTotal();

                DTODetalleOrdenCompra dtoDetalleOrdenCompra =  DTODetalleOrdenCompra.builder()
                        .nombreProducto(ap.getArticulo().getNombre())
                        .cantidad(dtoDet.getCantidad())
                        .costo(ap.getCostoUnitario())
                        .subTotal(dtoDet.getSubTotal())
                        .build();

                dtoDetalles.add(dtoDetalleOrdenCompra);
            }

            oc.setTotal(total);

            // Persistir
            OrdenCompra guardada = repositorioOrdenCompra.save(oc);

            DTOOrdenCompra dtoOrdenCompra = DTOOrdenCompra.builder()
                    .total(total)
                    .proveedorNombre(proveedor.getNombreProveedor())
                    .detalles(dtoDetalles)
                    .build();

            dtoSalidaOrdenDeCompra.getOrdenesDeCompra().add(dtoOrdenCompra);
        }

        // 4. Devolver IDs de las órdenes generadas
        return dtoSalidaOrdenDeCompra;
    }

    @Transactional
    public void generacionAutomatica(List<Long> articuloIds) {

        if (articuloIds == null || articuloIds.isEmpty()) {
            return;
        }

        List<Long> pendientesId = buscarPendientes(articuloIds);

        // No pediremos los articulos que ya esten en una orden de compra pendiente
        if (!pendientesId.isEmpty()){
            articuloIds.removeAll(pendientesId);
        }

        List<Articulo> articulos = new ArrayList<>();

        for (Long id : articuloIds ){
            Articulo articulo = repositorioArticulo.findActivoById(id).orElseThrow(()->new CustomException("No se encontró el artículo vigente con id " + id));
            articulos.add(articulo);
        }

        if (articulos.isEmpty()) {
            throw new CustomException("No se encontraron artículos válidos para generar la orden de compra");
        }

        //    Para cada Artículo, buscamos su ArticuloProveedor predeterminado.
        //    Si un Artículo no tiene ArticuloProveedor predeterminado, lanzar excepción.
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

        // Ahora recorremos cada entrada del Map y generamos una OrdenCompra única por proveedor.
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

    public DTOSugerirOrdenDetalle sugerirOrden(Long idArticulo){

        ArticuloProveedor articuloProveedorPredeterminado = repositorioArticuloProveedor.findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(idArticulo).orElseThrow(()-> new CustomException("No existe el articulo proveedor con id " + idArticulo));

        Articulo articulo = articuloProveedorPredeterminado.getArticulo();

        List<ArticuloProveedor> articuloProveedors = repositorioArticuloProveedor.findActivosByArticuloId(idArticulo);

        DTOSugerirOrdenDetalle dtoSugerirOrdenDetalle = DTOSugerirOrdenDetalle.builder().build();

        List<DTOProveedor> dtoProveedors = new ArrayList<>();

        for (ArticuloProveedor articuloProveedor : articuloProveedors){
            Proveedor proveedor = articuloProveedor.getProveedor();

            DTOProveedor dtoProveedor = DTOProveedor.builder()
                    .proveedorId(proveedor.getId())
                    .nombreProvedor(proveedor.getNombreProveedor())
                    .isPredeterminado(articuloProveedor.isPredeterminado())
                    .costoUnitario(articuloProveedor.getCostoUnitario())
                    .build();

            dtoProveedors.add(dtoProveedor);
        }

        dtoSugerirOrdenDetalle.setProveedores(dtoProveedors);

        if (articulo.getPuntoPedido()!=null){
            dtoSugerirOrdenDetalle.setCantidadPredeterminada(articuloProveedorPredeterminado.getLoteOptimo());
        } else {
            int cantidad = articuloProveedorPredeterminado.getCantidadTiempoFijo(repositorioOrdenCompraDetalle);
            dtoSugerirOrdenDetalle.setCantidadPredeterminada(cantidad);
        }

        return dtoSugerirOrdenDetalle;

    }

    // Trae los articulos vigentes
    public List<Articulo> traerTodos() {
        return repositorioArticulo.findByfhBajaArticuloIsNull();
    }

    // Trae las ordenes de compra vigentes
    public List<OrdenCompra> traerOrdenes() {
        return repositorioOrdenCompra.obtenerOrdenesVigentes();
    }

}
