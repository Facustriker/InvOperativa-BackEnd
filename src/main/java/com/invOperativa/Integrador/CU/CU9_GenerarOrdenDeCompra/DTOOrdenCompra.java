package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOOrdenCompra {

    private float total;
    private String proveedorNombre;
    private List<DTODetalleOrdenCompra> detalles;

}
