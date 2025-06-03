package com.invOperativa.Integrador.CU.CU1_GenerarVenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetalleGenerarVenta {

    private Long articuloID;
    private int cantidad;
    private float subTotal;

}
