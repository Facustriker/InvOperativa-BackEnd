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

    public boolean confirmar(Long idArticulo, int stock, boolean forzarConfirmacion) {
        Optional<Articulo> articuloOpt = repositorioArticulo.getArticuloVigentePorId(idArticulo);

        if (articuloOpt.isEmpty()) {
            throw new CustomException("Error, el articulo no se encontró");
        }

        Articulo articulo = articuloOpt.get();
        int puntoPedido = articulo.getPuntoPedido();

        if (stock < puntoPedido && !forzarConfirmacion) {
            // Mostrar advertencia sin guardar
            return true;
        }

        if(stock > articulo.getInventarioMaxArticulo()){
            throw new CustomException("Error, el stock ingresado es mayor que el tamaño maximo de inventario para este articulo");
        }

        // Guardar el nuevo stock
        articulo.setStock(stock);
        repositorioArticulo.save(articulo);

        // Si stock es menor al punto pedido, pero fue forzado, igualmente devolvemos true (para cartel amarillo)
        return stock < puntoPedido;
    }

}
