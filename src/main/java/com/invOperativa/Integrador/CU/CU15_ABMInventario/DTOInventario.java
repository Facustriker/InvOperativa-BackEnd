package com.invOperativa.Integrador.CU.CU15_ABMInventario;

import com.invOperativa.Integrador.Entidades.InventarioArticulo;
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
public class DTOInventario {

    private Long idInventario;

    @Builder.Default
    private Collection<DTOInventarioArticulo> inventarioArticulos = new ArrayList<>();
}
