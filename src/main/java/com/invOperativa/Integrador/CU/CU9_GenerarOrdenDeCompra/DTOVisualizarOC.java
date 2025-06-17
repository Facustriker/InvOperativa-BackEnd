package com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTODetallesOC;
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
public class DTOVisualizarOC {

    private Long idOrdenDeCompra;

    private float total;

    private String estado;

    @Builder.Default
        Collection<String> articulos = new ArrayList<>();

    public void addNombre(String nombre){
        articulos.add(nombre);
    }
}
