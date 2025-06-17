package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOProveedor {

    private Long idProveedor;

    private String nombreProveedor;

    private float costoPedido;

    private float costoUnitario;
}
