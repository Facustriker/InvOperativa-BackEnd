package com.invOperativa.Integrador.CU.CU8_CancelarOrdenDeCompra;

import com.invOperativa.Integrador.CU.CU4_ModificarOrdenDeCompra.DTOModificarOrdenCompra;
import com.invOperativa.Integrador.CU.CU9_GenerarOrdenDeCompra.DTOSalidaOrdenDeCompra;
import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ExpertoCancelarOrdenDeCompra {

    @Autowired
    RepositorioOrdenCompra repositorioOrdenCompra;

    public DTOModificarOrdenCompra getDatosOC(Long idOC) {
        Optional<OrdenCompra> ordenCompra = repositorioOrdenCompra.obtenerOCVigentePorID(idOC);

        DTOSalidaOrdenDeCompra

        if (ordenCompra.isEmpty()) {
            throw new CustomException("Error, no se encontró la orden de compra seleccionada");
        }



        return dto
    }

}
