package com.invOperativa.Integrador.CU.CU12_AjustarInventario;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.Articulo;
import com.invOperativa.Integrador.Repositorios.RepositorioArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoAjustarInventario {

    @Autowired
    private final RepositorioArticulo repositorioArticulo;

    public DTOAjustarInventario getArticulo(Long idArticulo){
        Optional<Articulo> articulo = repositorioArticulo.getArticuloVigentePorId(idArticulo);

        if(articulo.isEmpty()){
            throw new CustomException("Error, el articulo no se encontro");
        }

        return DTOAjustarInventario.builder()
                .nombre(articulo.get().getNombre())
                .cantidad(articulo.get().getStock())
                .build();
    }

    public boolean confirmar(Long idArticulo, int stock){
        Optional<Articulo> articulo = repositorioArticulo.getArticuloVigentePorId(idArticulo);

        if(articulo.isEmpty()){
            throw new CustomException("Error, el articulo no se encontro");
        }

        int puntoPedido = articulo.get().getPuntoPedido();

        //Si hay que mandar la notificación de la orden de compra el booleano es True, si está todo ok, es False
        return stock < puntoPedido;
    }
}
