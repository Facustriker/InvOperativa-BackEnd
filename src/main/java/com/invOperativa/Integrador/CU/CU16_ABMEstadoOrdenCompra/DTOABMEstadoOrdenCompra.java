package com.invOperativa.Integrador.CU.CU16_ABMEstadoOrdenCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOABMEstadoOrdenCompra {

    private Long idEOC;

    private Date fhBajaEOC;

    private String nombreEstado;

    private boolean dadoBaja;
}
