package com.invOperativa.Integrador.CU.CU3_ListarProductosFaltantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOArticulo {

    private Long id;
    private float costoAlmacenamiento;
    private String nombreArt;
    private String descripcionArt;
    private Date fhBajaArticulo;
    private int inventarioMaxArticulo;
    private int loteOptimo;
    private float precioUnitario;
    private Date proximaRevision;
    private int puntoPedido;
    private int stock;
    private int stockSeguridad;

}
