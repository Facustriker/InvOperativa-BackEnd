package com.invOperativa.Integrador.CU.CU17_ABMModeloInventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DTOABMModeloInventario {

    private Long idMI;

    private Date fhBajaMI;

    private String nombreMI;

    private boolean MIdadoBaja;

}
