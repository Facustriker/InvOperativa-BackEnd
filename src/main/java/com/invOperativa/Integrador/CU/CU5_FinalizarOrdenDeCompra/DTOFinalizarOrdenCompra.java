package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOFinalizarOrdenCompra {

    private Long idOC;

    private Date fhAltaOC;

    @Builder.Default
    Collection<DTODetallesOC> detallesOC = new ArrayList<>();

    public void addDetalle(DTODetallesOC dtoDetalle){
        detallesOC.add(dtoDetalle);
    }
}
