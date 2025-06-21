package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

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
public class DTORespuestaFinalizarOC {
    private boolean requiereAtencion;
    
    @Builder.Default
    private Collection<DTODetalleAtencion> detallesAtencion = new ArrayList<>();
}
