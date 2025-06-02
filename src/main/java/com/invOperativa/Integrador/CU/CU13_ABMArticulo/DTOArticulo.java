package com.invOperativa.Integrador.CU.CU13_ABMArticulo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOArticulo {

    private float costoAlmacenamiento;
    private int demanda;
    private String descripcionArt;
    private int inventarioMaxArticulo;
    private String nombre;
    private float precioUnitario;
    private int stock;

}
