package com.invOperativa.Integrador.CU.CU6_EnviarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODetallesOC;
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
public class DTOEnviarOrdenCompra {

    private Long idOC;

    private Date fhAltaOC;

    private String nombreEstado;

    @Builder.Default
    Collection<DTODetallesOCEnviar> detallesOC = new ArrayList<>();

    public void addDetalle(DTODetallesOCEnviar dtoDetalle){
        detallesOC.add(dtoDetalle);
    }
}
