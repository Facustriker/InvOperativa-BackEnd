package com.invOperativa.Integrador.CU.CU2_ListarProductosAReponer;

import com.invOperativa.Integrador.Entidades.Articulo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOInventarioArticulo {

    //Atributos del InvArt
    private Long idInvArt;

    private int inventarioMaxArticulo;

    private int loteOptimo;

    private int puntoPedido;

    private int stock;

    private int stockSeguridad;

    //Atributos del Articulo
    private Long idArt;

    private float cargosPedido;

    private String descripcionArt;

    private float precioUnitario;

}
