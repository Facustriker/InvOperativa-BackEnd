package com.invOperativa.Integrador.CU.CU6_EnviarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetallesOCEnviar {

    private int cantidad;

    private float subTotal;

    private float costoUnitario;

    private float costoPedido;

    private boolean isProveedorPredeterminado;

    private String nombreArt;

    private float costoAlmacenamientoArt;

    private String nombreProveedor;
}
