package com.invOperativa.Integrador.CU.CU18_CalcularCGI;

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
public class DTOCalcularCGI {

    private String nombreArticulo;

    @Builder.Default
    Collection<DTODatosCGI> datosCGI = new ArrayList<>();

    public void addDato(DTODatosCGI dtoDato){
        datosCGI.add(dtoDato);
    }
}
