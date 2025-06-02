package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTODetallesDatosMod {

    private Long idOCDetalle;

    private int cantidad;

    private Long idProveedor;
}
