package com.invOperativa.Integrador.Borrador.CU99_CrearCoso;

import com.invOperativa.Integrador.Borrador.Coso;
import com.invOperativa.Integrador.Borrador.RepositorioCoso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ExpertoCrearCoso {

    private final RepositorioCoso repositorioCoso;

    public Long crear(DTOCrearCoso request) throws Exception {

        Coso coso = Coso.builder()
                .nombreArticulo(request.getNombreCoso())
                .fhaArticulo(new Date())
                .build();

        repositorioCoso.save(coso);

        return coso.getId();
    }
}
