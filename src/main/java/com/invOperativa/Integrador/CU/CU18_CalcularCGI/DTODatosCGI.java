package com.invOperativa.Integrador.CU.CU18_CalcularCGI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODatosCGI {

    private String nombreProveedor;

    private float CGI;

    private float costoCompra;

    private float costoPedido;

    private float costoAlmacenamiento;
}
