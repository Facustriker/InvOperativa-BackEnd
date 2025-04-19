package com.invOperativa.Integrador.CU.CU99_CrearArticulo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOCrearArticulo {

    private String nombreArticulo;

}
