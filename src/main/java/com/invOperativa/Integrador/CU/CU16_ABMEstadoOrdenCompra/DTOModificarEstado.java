package com.invOperativa.Integrador.CU.CU16_ABMEstadoOrdenCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOModificarEstado {

    private Long id;
    private String nuevoNombre;

}
