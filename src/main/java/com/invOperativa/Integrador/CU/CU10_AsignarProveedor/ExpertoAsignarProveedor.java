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

        ModeloInventario modeloInventario = repositorioModeloInventario.findActivoById(dto.getModeloInventarioId()).orElseThrow(()->new CustomException("No existe el modelo de inventario"));

        revisarDTO(dto, modeloInventario);

        Articulo articulo = repositorioArticulo.findActivoById(dto.getArticuloId()).orElseThrow(()->new CustomException("No existe el articulo"));

        Proveedor proveedor = repositorioProveedor.findActivoById(dto.getProveedorId()).orElseThrow(()->new CustomException("No existe el proveedor"));

        // Verificar quien es el proveedor predeterminado
        Optional<ArticuloProveedor> proveedorPredeterminado = repositorioArticuloProveedor.findByArticuloIdAndIsPredeterminadoTrueAndFechaBajaIsNull(articulo.getId());

        // Si no hay un predeterminado, entonces este lo sera
        if (proveedorPredeterminado.isEmpty()){

            dto.setPredeterminado(true);

        } else {

            // Si este viene a ser el predeterminado, entonces hay que dejar de hacer predeterminado al anterior
            if (dto.isPredeterminado()){

                ArticuloProveedor proveedorAnterior = proveedorPredeterminado.get();
                proveedorAnterior.setPredeterminado(false);

                repositorioArticuloProveedor.save(proveedorAnterior);

            }

        }

        // Creamos el articulo proveedor con los datos que tenemos
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
                .build();

        // Verificamos si el modelo es de tiempo fijo
        if (modeloInventario.getNombreModelo().equals("Tiempo fijo")){

            LocalDate fechaActual = LocalDate.now();
            Date hoy = new Date();

            // Si no viene una fecha de proxima revision se calcula una nueva
            if (dto.getProximaRevision() == null){

                int dias = dto.getTiempoFijo();

                LocalDate proximaRevision = fechaActual.plusDays(dias);

                // Convertir a Date usando la zona horaria del sistema
                Date fechaProximaRevision = Date.from(proximaRevision.atStartOfDay(ZoneId.systemDefault()).toInstant());

                articulo.setTiempoFijo(dias);
                articulo.setProximaRevision(fechaProximaRevision);

                repositorioArticulo.save(articulo);

            } else { // Si ya viene una simplemente se aisgna

                // Verificamos que la fecha sea valida
                if (dto.getProximaRevision().before(hoy)){
                    throw new CustomException("La próxima fecha no puede ser en el pasado");
                }

                articulo.setProximaRevision(dto.getProximaRevision());
                articulo.setTiempoFijo(dto.getTiempoFijo());

                repositorioArticulo.save(articulo);

            }

        } else { // En caso de que el modelo sea de Lote fijo

            int demanda = articulo.getDemanda();
            float tiempoEntrega = dto.getDemoraEntrega();
            float nivelServicio = dto.getNivelServicio();
            double z = ArticuloProveedor.getZ(nivelServicio);
            double desviacion = 0.25F * Math.sqrt(tiempoEntrega);

            int puntoPedido = (int) Math.round((demanda / 365.0) * tiempoEntrega + z * desviacion);

            int stockSeguridad = (int) Math.round(z * desviacion);

            articuloProveedor.setStockSeguridad(stockSeguridad);

            articulo.setPuntoPedido(puntoPedido);

            repositorioArticulo.save(articulo);

        }

        // Calcular lote optimo

        int loteOptimo = (int) Math.sqrt((2*articulo.getDemanda()*dto.getCostoPedido())/(articulo.getCostoAlmacenamiento()));

        articuloProveedor.setLoteOptimo(loteOptimo);


        repositorioArticuloProveedor.save(articuloProveedor);
    }

    @Transactional
    public void modificarAsignacion(DTOAsignarProveedor dto){
    }

    @Transactional
    public void eliminarAsignacion (Long id){

        ArticuloProveedor articuloProveedor = repositorioArticuloProveedor.findById(id).orElseThrow(()->new CustomException("No existe el articulo proveedor"));

        List<OrdenCompraDetalle> enUso = repositorioOrdenCompraDetalle.findByArticuloProveedorEnOrdenesPendientesOEnviadas(articuloProveedor.getId());
        if (!enUso.isEmpty()) {
            throw new CustomException("No se puede dar de baja el Artículo-Proveedor porque está en órdenes pendientes o enviadas.");
        }

        articuloProveedor.setFechaBaja(new Date());

        repositorioArticuloProveedor.save(articuloProveedor);

    }

}





























