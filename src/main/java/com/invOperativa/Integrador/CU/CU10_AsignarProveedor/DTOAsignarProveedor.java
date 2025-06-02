package com.invOperativa.Integrador.CU.CU10_AsignarProveedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOAsignarProveedor {
    private Long articuloId;
    private Long proveedorId;
    private Long modeloInventarioId;
    private float costoPedido;
    private float costoUnitario;
    private int demoraEntrega;
    private boolean isPredeterminado;
}
