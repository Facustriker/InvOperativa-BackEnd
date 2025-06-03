package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.*;
import com.invOperativa.Integrador.Repositorios.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoAsignarProveedor {

    @Autowired
    private RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    private RepositorioProveedor repositorioProveedor;

    @Autowired
    private RepositorioArticulo repositorioArticulo;

    @Autowired
    private RepositorioModeloInventario repositorioModeloInventario;

    @Autowired
    private RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle;

    public void revisarDTO (DTOAsignarProveedor dto, ModeloInventario modelo){

        if (dto.getCostoPedido() < 0) {
            throw new CustomException("El costo del pedido no puede ser negativo");
        }

        if (dto.getCostoUnitario() <= 0) {
            throw new CustomException("El costo unitario debe ser mayor a 0");
        }

        if (dto.getNivelServicio() < 0 || dto.getNivelServicio() > 1) {
            throw new CustomException("El nivel de servicio no puede ser menor a cero ni mayor a 1");
        }

        if (dto.getDemoraEntrega() < 0) {
            throw new CustomException("La demora en la entrega no puede ser negativa");
        }

        if (modelo.getNombreModelo().equals("Tiempo fijo")){

            if (dto.getTiempoFijo() <= 0){
                throw new CustomException("El tiempo entre revisiones no puede ser negativo ni cero");
            }

        }
    }

    @Transactional
    public void asignarProveedor(DTOAsignarProveedor dto) {
        ModeloInventario modeloInventario = repositorioModeloInventario.findActivoById(dto.getModeloInventarioId())
                .orElseThrow(() -> new CustomException("No existe el modelo de inventario"));

        revisarDTO(dto, modeloInventario);

        Articulo articulo = repositorioArticulo.findActivoById(dto.getArticuloId())
                .orElseThrow(() -> new CustomException("No existe el artículo"));

        Proveedor proveedor = repositorioProveedor.findActivoById(dto.getProveedorId())
                .orElseThrow(() -> new CustomException("No existe el proveedor"));

        Optional<ArticuloProveedor> proveedorPredeterminado =
                repositorioArticuloProveedor.findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(articulo.getId());

        if (proveedorPredeterminado.isEmpty()) {
            dto.setPredeterminado(true);
        } else if (dto.isPredeterminado()) {
            ArticuloProveedor proveedorAnterior = proveedorPredeterminado.get();
            proveedorAnterior.setPredeterminado(false);
            repositorioArticuloProveedor.save(proveedorAnterior);
        }

        ArticuloProveedor articuloProveedor = ArticuloProveedor.builder()
                .articulo(articulo)
                .proveedor(proveedor)
                .modeloInventario(modeloInventario)
                .costoPedido(dto.getCostoPedido())
                .costoUnitario(dto.getCostoUnitario())
                .demoraEntrega(dto.getDemoraEntrega())
                .fhAsignacion(new Date())
                .isPredeterminado(dto.isPredeterminado())
                .nivelServicio(dto.getNivelServicio())
                .stockSeguridad(null)
                .loteOptimo(null)
                .build();

        if (articuloProveedor.isPredeterminado()){
            if (modeloInventario.getNombreModelo().equals("Tiempo fijo")) {
                LocalDate fechaActual = LocalDate.now();
                Date hoy = new Date();

                int dias = dto.getTiempoFijo();

                Date proxima;
                if (dto.getProximaRevision() == null) {
                    proxima = Date.from(fechaActual.plusDays(dias).atStartOfDay(ZoneId.systemDefault()).toInstant());
                } else {
                    if (dto.getProximaRevision().before(hoy)) {
                        throw new CustomException("La próxima fecha no puede ser en el pasado");
                    }
                    proxima = dto.getProximaRevision();
                }

                articulo.setTiempoFijo(dias);
                articulo.setProximaRevision(proxima);

                // Borrar campos del otro modelo
                articulo.setPuntoPedido(null);
                articuloProveedor.setStockSeguridad(null);
                articuloProveedor.setLoteOptimo(null);

                repositorioArticulo.save(articulo);

            } else {
                float tiempoEntrega = dto.getDemoraEntrega();
                float nivelServicio = dto.getNivelServicio();
                double z = ArticuloProveedor.getZ(nivelServicio);
                double desviacion = 0.25F * Math.sqrt(tiempoEntrega);

                int stockSeguridad = (int) Math.round(z * desviacion);
                articuloProveedor.setStockSeguridad(stockSeguridad);

                int demanda = articulo.getDemanda();
                int puntoPedido = (int) Math.round((demanda / 365.0) * tiempoEntrega + z * desviacion);
                articulo.setPuntoPedido(puntoPedido);

                // Borrar campos del otro modelo
                articulo.setTiempoFijo(null);
                articulo.setProximaRevision(null);

                int loteOptimo = (int) Math.sqrt((2 * articulo.getDemanda() * dto.getCostoPedido()) / articulo.getCostoAlmacenamiento());
                articuloProveedor.setLoteOptimo(loteOptimo);

                repositorioArticulo.save(articulo);
            }
        }

        repositorioArticuloProveedor.save(articuloProveedor);
    }

    @Transactional
    public void modificarAsignacion(DTOAsignarProveedor dto) {
        ArticuloProveedor articuloProveedor = repositorioArticuloProveedor.findById(dto.getId())
                .orElseThrow(() -> new CustomException("No existe la asignación a modificar"));

        if (articuloProveedor.getFechaBaja() != null) {
            throw new CustomException("No se puede modificar una asignación dada de baja");
        }

        Articulo articulo = articuloProveedor.getArticulo();

        ModeloInventario nuevoModelo = repositorioModeloInventario.findActivoById(dto.getModeloInventarioId())
                .orElseThrow(() -> new CustomException("No existe el nuevo modelo de inventario"));

        revisarDTO(dto, nuevoModelo);

        articuloProveedor.setModeloInventario(nuevoModelo);
        articuloProveedor.setCostoPedido(dto.getCostoPedido());
        articuloProveedor.setCostoUnitario(dto.getCostoUnitario());
        articuloProveedor.setDemoraEntrega(dto.getDemoraEntrega());
        articuloProveedor.setNivelServicio(dto.getNivelServicio());

        boolean eraPredeterminado = articuloProveedor.isPredeterminado();

        if (!eraPredeterminado && dto.isPredeterminado()) {
            repositorioArticuloProveedor.findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(articulo.getId())
                    .ifPresent(ap -> {
                        ap.setPredeterminado(false);
                        repositorioArticuloProveedor.save(ap);
                    });
            articuloProveedor.setPredeterminado(true);
        } else {
            articuloProveedor.setPredeterminado(dto.isPredeterminado());
        }

        if (articuloProveedor.isPredeterminado()){
            if (nuevoModelo.getNombreModelo().equals("Tiempo fijo")) {
                LocalDate fechaActual = LocalDate.now();
                Date hoy = new Date();

                int dias = dto.getTiempoFijo();
                Date proxima;

                if (dto.getProximaRevision() == null) {
                    proxima = Date.from(fechaActual.plusDays(dias).atStartOfDay(ZoneId.systemDefault()).toInstant());
                } else {
                    if (dto.getProximaRevision().before(hoy)) {
                        throw new CustomException("La próxima fecha no puede ser en el pasado");
                    }
                    proxima = dto.getProximaRevision();
                }

                articulo.setTiempoFijo(dias);
                articulo.setProximaRevision(proxima);

                // Limpiar datos de lote fijo
                articulo.setPuntoPedido(null);
                articuloProveedor.setStockSeguridad(null);
                articuloProveedor.setLoteOptimo(null);

                repositorioArticulo.save(articulo);

            } else {
                float tiempoEntrega = dto.getDemoraEntrega();
                float nivelServicio = dto.getNivelServicio();
                double z = ArticuloProveedor.getZ(nivelServicio);
                double desviacion = 0.25F * Math.sqrt(tiempoEntrega);
                int stockSeguridad = (int) Math.round(z * desviacion);

                articuloProveedor.setStockSeguridad(stockSeguridad);

                if (articuloProveedor.isPredeterminado()) {
                    int demanda = articulo.getDemanda();
                    int puntoPedido = (int) Math.round((demanda / 365.0) * tiempoEntrega + z * desviacion);
                    articulo.setPuntoPedido(puntoPedido);
                }

                // Limpiar datos de tiempo fijo
                articulo.setTiempoFijo(null);
                articulo.setProximaRevision(null);

                int loteOptimo = (int) Math.sqrt((2 * articulo.getDemanda() * dto.getCostoPedido()) / articulo.getCostoAlmacenamiento());
                articuloProveedor.setLoteOptimo(loteOptimo);

                repositorioArticulo.save(articulo);
            }
        }

        repositorioArticuloProveedor.save(articuloProveedor);
    }

    @Transactional
    public void eliminarAsignacion(Long id) {

        ArticuloProveedor articuloProveedor = repositorioArticuloProveedor
                .findById(id)
                .orElseThrow(() -> new CustomException("No existe el artículo proveedor"));

        List<OrdenCompraDetalle> enUso = repositorioOrdenCompraDetalle
                .findByArticuloProveedorEnOrdenesPendientesOEnviadas(articuloProveedor.getId());

        if (!enUso.isEmpty()) {
            throw new CustomException("No se puede dar de baja el Artículo-Proveedor porque está en órdenes pendientes o enviadas.");
        }

        List<ArticuloProveedor> articuloProveedors = repositorioArticuloProveedor
                .findActivosByArticuloId(articuloProveedor.getArticulo().getId());

        if (articuloProveedors.size() == 1) {
            throw new CustomException("No se puede dar de baja el Artículo-Proveedor porque es el único.");
        }

        boolean eraPredeterminado = articuloProveedor.isPredeterminado();

        if (eraPredeterminado) {
            throw new CustomException("No se puede dar de baja el Artículo-Proveedor porque es el predeterminado.");
        }

        // Dar de baja lógica
        articuloProveedor.setFechaBaja(new Date());
        articuloProveedor.setPredeterminado(false);
        repositorioArticuloProveedor.save(articuloProveedor);
    }

}





























