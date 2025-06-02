package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

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
public class DTODatosModificacion {

    private Long idOC;

    @Builder.Default
    Collection<DTODetallesDatosMod> detallesMod = new ArrayList<>();

    public void addDetalle(DTODetallesDatosMod dtoDetalle){
        detallesMod.add(dtoDetalle);
    }
}
