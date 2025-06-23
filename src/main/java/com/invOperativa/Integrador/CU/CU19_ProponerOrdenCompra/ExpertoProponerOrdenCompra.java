package com.invOperativa.Integrador.CU.CU19_ProponerOrdenCompra;

import com.invOperativa.Integrador.Entidades.*;
import com.invOperativa.Integrador.Repositorios.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ExpertoProponerOrdenCompra {

    @Autowired
    protected RepositorioArticuloProveedor repositorioArticuloProveedor;

    @Autowired
    protected RepositorioProveedor repositorioProveedor;

    @Autowired
    protected RepositorioOrdenCompra repositorioOC;

    @Autowired
    protected RepositorioOrdenCompraDetalle repositorioOCDetalle;

    @Autowired
    protected RepositorioEstadoOrdenCompra repositorioEstadoOC;

    @Scheduled(cron = "0 0 8 * * ?") //Se ejecuta todos los días a las 8 AM
    public void generarOrdenCompra() {
        System.out.println("Ejecutando tarea programada...");
        //Obtengo todos los proveedores
        Collection<Proveedor> proveedores = repositorioProveedor.findAll();

        EstadoOrdenCompra estadoOrdenCompra = repositorioEstadoOC.findByNombreEstadoOrdenCompraAndFhBajaEstadoOrdenCompraIsNull("Pendiente");

        //Por cada proveedor busco los articulos con modelo de inventario tiempo fijo y fechaRevision igual a hoy
        for (Proveedor proveedor : proveedores) {
            Collection<ArticuloProveedor> articulosProveedor = repositorioArticuloProveedor.findTiempoFijo(proveedor.getId());
            System.out.println(articulosProveedor);

            if (articulosProveedor.isEmpty()) {
                continue;
            }
            System.out.println("Hay articulos:");
            OrdenCompra ordenCompra = OrdenCompra.builder()
                    .fhAltaOrdenCompra(new Date())
                    .isAuto(true)
                    .estadoOrdenCompra(estadoOrdenCompra)
                    .proveedor(proveedor)
                    .build();

            for (ArticuloProveedor ap : articulosProveedor) {
                Collection<OrdenCompra> ordenesExistentes = repositorioOC.findOrdenesNoFinalizadasNiCanceladasByArticuloProveedor(ap);
                System.out.println("orden");
                System.out.println(ordenesExistentes);
                if (!ordenesExistentes.isEmpty()) {
                    System.out.println("ya existe la orden");
                    // Ya hay una orden "activa" para este artículoProveedor. Saltar al siguiente.
                    continue;
                }

                // int cantidad = ap.getArticulo().getPuntoPedido() - ap.getArticulo().getStock();
                // getCantidadTiempoFijo: con este metodo obtenemos la cantidad a pedir
                int cantidad = ap.getCantidadTiempoFijo(repositorioOCDetalle);
                if (cantidad <= 0) {
                    System.out.println("El producto tiene existencias suficientes" + ap.getArticulo().getNombre());
                    continue;
                }
                float subtotal = 0;
                subtotal = cantidad * ap.getCostoUnitario() + ap.getCostoPedido();

                OrdenCompraDetalle OCDetalle = OrdenCompraDetalle.builder()
                        .articuloProveedor(ap)
                        .cantidad(cantidad)
                        .subTotal(subtotal)
                        .build();
                ordenCompra.addOrdenCompraDetalle(OCDetalle);
                ap.getArticulo().calcularProximaRevision();
            }
            if (!ordenCompra.getOrdenCompraDetalles().isEmpty()) {
                repositorioOC.save(ordenCompra);
                System.out.println("Orden generada: " + ordenCompra.getId() + "para el proveedor: " + proveedor.getNombreProveedor());
            } else {
                System.out.println("No se creó la orden para proveedor " + proveedor.getNombreProveedor() + " porque no había artículos sin orden existente.");
            }
        }
    }
}