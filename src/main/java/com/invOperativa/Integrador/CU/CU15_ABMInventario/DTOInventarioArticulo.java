package com.invOperativa.Integrador.CU.CU15_ABMInventario;

import com.invOperativa.Integrador.Entidades.InventarioArticulo;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOInventarioArticulo {

    private Long idInventarioArticulo;

    private int inventarioMaxArticulo;

    private int loteOptimo;

    private int puntoPedido;

    private int stock;

    private int stockSeguridad;

    private String descripcionArticulo;

    private int precio;
}
