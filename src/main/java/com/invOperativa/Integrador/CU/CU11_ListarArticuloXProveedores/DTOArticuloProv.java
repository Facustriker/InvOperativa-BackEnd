package com.invOperativa.Integrador.CU.CU11_ListarArticuloXProveedores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOArticuloProv {

    private Long idArticulo;

    private String nombreArticulo;

    private String modeloInventario;

    private Boolean isPredeterminado;

    private String descripcionArticulo;

    private float precioUnitario;

    private float costoUnitario;

    private int demoraEntrega;

    private float costoPedido;

    private int stock;

}
