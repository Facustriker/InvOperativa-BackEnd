package com.invOperativa.Integrador.CU.CU1_GenerarVenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOVenta {

    public float monto;
    public Long id;
    public Date fechaAlta;
    public int cantidadArticulos;

}
