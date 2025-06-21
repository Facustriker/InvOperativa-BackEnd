package com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU18_CalcularCGI.DTODatosCGI;
import com.invOperativa.Integrador.Entidades.Proveedor;
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
public class DTOModificarOrdenCompra {

    private Long idOC;

    private Date fhAltaOC;

    @Builder.Default
    Collection<DTODetallesOC> detallesOC = new ArrayList<>();

    public void addDetalle(DTODetallesOC dtoDetalle){
        detallesOC.add(dtoDetalle);
    }

    @Builder.Default
    Collection<DTOProveedor> proveedores = new ArrayList<>();

    public void addProveedor(DTOProveedor p){
        proveedores.add(p);
    }
}
