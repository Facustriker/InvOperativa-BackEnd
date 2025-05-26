package com.invOperativa.Integrador.CU.CU8_CancelarOrdenDeCompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/CancelarOrdenDeCompra")
public class ControladorCancelarOrdenDeCompra {

    @Autowired
    protected ExpertoCancelarOrdenDeCompra experto;

}
