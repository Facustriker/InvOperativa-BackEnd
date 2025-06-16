package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOProveedor {
    private Long proveedorId;
    private String nombreProvedor;
    private boolean isPredeterminado;
    private float costoUnitario;
}
