package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetalleOrden {

    private int cantidad;
    private float subTotal;
    private Long articuloProveedorId;

}
