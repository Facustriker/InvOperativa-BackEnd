package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTORespuestaFinalizarOC {
    private boolean requiereAtencion;
    
    @Builder.Default
    private List<String> nombresArticulosAtencion = new ArrayList<>();
    
    @Builder.Default
    private List<Integer> stocksActuales = new ArrayList<>();
    
    @Builder.Default
    private List<Integer> puntosPedido = new ArrayList<>();

    public void addArticuloAtencion(String nombreArticulo, int stockActual, int puntoPedido) {
        nombresArticulosAtencion.add(nombreArticulo);
        stocksActuales.add(stockActual);
        puntosPedido.add(puntoPedido);
    }
}
