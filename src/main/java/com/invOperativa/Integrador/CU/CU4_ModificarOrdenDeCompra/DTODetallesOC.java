package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetallesOC {

    private Long idOCDetalle;

    private int cantidad;

    private float subTotal;

    private float costoUnitario;

    private float costoPedido;

    private String nombreArt;

    private float costoAlmacenamientoArt;

    private String nombreProveedor;

    private boolean isPredeterminado;

    private int puntoPedido;

    private int stock;

}
