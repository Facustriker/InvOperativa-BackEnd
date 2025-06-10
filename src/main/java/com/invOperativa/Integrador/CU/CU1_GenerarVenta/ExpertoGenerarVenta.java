package com.invOperativa.Integrador.CU.CU1_GenerarVenta;

import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.ExpertoGenerarOrdenDeCompra;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Entidades.DetalleVenta;
import com.invOperativa.Integrador.Entidades.Venta;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import com.invOperativa.Integrador.Repositorios.RepositorioDetalleVenta;
import com.invOperativa.Integrador.Repositorios.RepositorioVenta;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertoGenerarVenta {

    private final RepositorioArticulo repositorioArticulo;
    private final RepositorioDetalleVenta repositorioDetalleVenta;
    private final RepositorioVenta repositorioVenta;
    private final ExpertoGenerarOrdenDeCompra expertoGenerarOrdenDeCompra;

    // Genera una nueva venta y verifica si hace falta generar ordenes de compra
    @Transactional
    public void nueva(DTOGenerarVenta dto) {

        List<Long> articulosComprar = new ArrayList<>();

        List<DTODetalleGenerarVenta> detalles = dto.getDetalles();

        if (detalles.isEmpty()) {
            throw new CustomException("La venta debe contener al menos un artículo");
        }

        Venta venta = Venta.builder()
                .fhAltaVenta(new Date())
                .montoTotal(0)
                .build();

        List<DetalleVenta> detallesAuxiliares = new ArrayList<>();

        float totalAuxiliar = 0;

        for (DTODetalleGenerarVenta detalle: detalles){

            Articulo articulo =  repositorioArticulo.findActivoById(detalle.getArticuloID()).orElseThrow(()-> new CustomException("No existe el artículo que se desea vender"));

            int nuevoStock = articulo.getStock()-detalle.getCantidad();

            if (nuevoStock < 0){
                throw new CustomException("La cantidad ingresada es mayor al stock disponible");
            }

            float subTotal = articulo.getPrecioUnitario() * detalle.getCantidad();

            if (Math.abs(detalle.getSubTotal() - subTotal) > 0.01f) {
                throw new CustomException("El subtotal del detalle tiene un valor incorrecto");
            }

            totalAuxiliar += detalle.getSubTotal();

            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .articulo(articulo)
                    .cant(detalle.getCantidad())
                    .subTotal(detalle.getSubTotal())
                    .build();


            articulo.setStock(nuevoStock);
            repositorioArticulo.save(articulo);

            if (articulo.getPuntoPedido() != null){
                if (nuevoStock <= articulo.getPuntoPedido()){
                    articulosComprar.add(articulo.getId());
                }
            }

            detallesAuxiliares.add(detalleVenta);

        }

        if (Math.abs(totalAuxiliar - dto.getTotal()) > 0.01f) {
            throw new CustomException("El total de la venta tiene un valor incorrecto");
        }

        venta.setMontoTotal(dto.getTotal());

        if (!articulosComprar.isEmpty()){
            expertoGenerarOrdenDeCompra.generacionAutomatica(articulosComprar);
        }

        for (DetalleVenta detalle : detallesAuxiliares){
            DetalleVenta detalleAuxiliar = repositorioDetalleVenta.save(detalle);
            venta.addDetalleVenta(detalleAuxiliar);
        }

        repositorioVenta.save(venta);

    }

    // Devuelve un DTO con todas las ventas ordenadas de las mas reciente a la mas antigua
    public List<DTOVenta> getAll(){

        List<DTOVenta> dtoVentas = new ArrayList<>();

        List<Venta> ventas = repositorioVenta.findAllByOrderByFhAltaVentaDesc();

        if (ventas.isEmpty()){ // Si no hay devolvemos la lista vacía
            return dtoVentas;
        }

        // Transformamos las ventas en DTO y las devolvemos
        for (Venta venta : ventas) {

            DTOVenta dto = DTOVenta.builder()
                    .id(venta.getId())
                    .monto(venta.getMontoTotal())
                    .fechaAlta(venta.getFhAltaVenta())
                    .cantidadArticulos(venta.getDetalleVentas().size())
                    .build();

            dtoVentas.add(dto);

        }

        return dtoVentas;

    }

}
