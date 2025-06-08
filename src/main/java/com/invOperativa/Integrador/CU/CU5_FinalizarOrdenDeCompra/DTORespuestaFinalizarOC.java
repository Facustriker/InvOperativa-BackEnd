package com.invOperativa.Integrador.CU.CU5_FinalizarOrdenDeCompra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTORespuestaFinalizarOC {
    private boolean requiereAtencion;
<<<<<<< Updated upstream
} 
=======
    
    @Builder.Default
    private List<String> nombresArticulosAtencion = new ArrayList<>();
    
    @Builder.Default
    private List<Integer> stocksActuales = new ArrayList<>();
    
    @Builder.Default
    private List<Integer> puntosPedido = new ArrayList<>();
    @Builder.Default
    private List<Long> idsDetalles = new ArrayList<>();

    @Builder.Default
    private List<Float> subtotalesDetalles = new ArrayList<>();

    public void addArticuloAtencion(String nombreArticulo, int stockActual, int puntoPedido, Long idDetalle, float subtotal) {
        
        nombresArticulosAtencion.add(nombreArticulo);
        stocksActuales.add(stockActual);
        puntosPedido.add(puntoPedido);
        idsDetalles.add(idDetalle);
        subtotalesDetalles.add(subtotal);
    }
}
>>>>>>> Stashed changes
