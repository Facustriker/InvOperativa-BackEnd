package com.invOperativa.Integrador.CU.CU14_ABMProveedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOProveedor {

    private Long idProveedor;

    private Date fhBajaProveedor;

    private boolean dadoBaja;

    private String nombreProveedor;
}
