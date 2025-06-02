package com.invOperativa.Integrador.CU.CU12_AjustarInventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOAjustarInventario {

    private String nombre;

    private int cantidad;
}
