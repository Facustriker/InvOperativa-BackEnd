package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOAsignarProveedor {

    private Long id;

    private Long articuloId;
    private Long proveedorId;
    private Long modeloInventarioId;

    private float costoPedido;
    private float costoUnitario;
    private int demoraEntrega;
    private boolean isPredeterminado;
    private int stockSeguridad;
    private float nivelServicio;

    private Date proximaRevision;
    private int tiempoFijo;
}
