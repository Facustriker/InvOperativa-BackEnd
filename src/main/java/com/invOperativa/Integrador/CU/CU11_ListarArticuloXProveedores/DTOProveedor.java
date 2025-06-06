package com.invOperativa.Integrador.CU.CU11_ListarArticuloXProveedores;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOProveedor {

    private Long idProv;

    private Date fhBajaProveedor;

    private String nombreProveedor;
}
