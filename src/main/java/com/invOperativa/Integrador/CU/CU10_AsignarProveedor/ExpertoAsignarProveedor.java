package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.*;
import com.invOperativa.Integrador.Repositorios.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public void revisarDTO (DTOAsignarProveedor dto){

        if (dto.getCostoPedido() < 0) {
            throw new CustomException("El costo del pedido no puede ser negativo");
        }

        if (dto.getDemoraEntrega() < 0) {
            throw new CustomException("La demora en la entrega no puede ser negativa");
        }

        if (dto.getCostoUnitario() <= 0) {
            throw new CustomException("El costo unitario debe ser mayor a 0");
        }

    }

    @Transactional
    public void asignarProveedor(DTOAsignarProveedor dto) {

        revisarDTO(dto);

        Articulo articulo = repositorioArticulo.findActivoById(dto.getArticuloId()).orElseThrow(()->new CustomException("No existe el articulo"));

        Proveedor proveedor = repositorioProveedor.findActivoById(dto.getProveedorId()).orElseThrow(()->new CustomException("No existe el proveedor"));

        ModeloInventario modeloInventario = repositorioModeloInventario.findActivoById(dto.getModeloInventarioId()).orElseThrow(()->new CustomException("No existe el modelo de inventario"));

        ArticuloProveedor articuloProveedor = ArticuloProveedor.builder()
                .articulo(articulo)
                .proveedor(proveedor)
                .modeloInventario(modeloInventario)
                .costoPedido(dto.getCostoPedido())
                .costoUnitario(dto.getCostoUnitario())
                .demoraEntrega(dto.getDemoraEntrega())
                .fhAsignacion(new Date())
                .isPredeterminado(dto.isPredeterminado())
                .build();

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





























