package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetalleAtencion {
    private String nombreArticulo;
    private int stockActual;
    private int puntoPedido;
    private float costoUnitario;
    private float costoPedido;
    private String nombreProveedor;
} 