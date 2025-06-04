package com.invOperativa.Integrador.CU.CU1_GenerarVenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOGenerarVenta {

    private List<DTODetalleGenerarVenta> detalles;
    private float total;

}
