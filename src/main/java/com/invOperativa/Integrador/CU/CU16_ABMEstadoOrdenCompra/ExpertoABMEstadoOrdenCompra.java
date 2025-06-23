package com.invOperativa.Integrador.CU.CU16_ABMEstadoOrdenCompra;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.EstadoOrdenCompra;
import com.invOperativa.Integrador.Entidades.OrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioEstadoOrdenCompra;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompra;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoABMEstadoOrdenCompra {

    @Autowired
    private final RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;

    @Autowired
    private final RepositorioOrdenCompra repositorioOrdenCompra;

    public Collection<DTOABMEstadoOrdenCompra> getEstados(boolean soloVigentes) {
        boolean dadoDeBaja;
        Collection<EstadoOrdenCompra> estados;

        if(soloVigentes){
            estados = repositorioEstadoOrdenCompra.getEstadosVigentes();
        }else{
            estados = repositorioEstadoOrdenCompra.findAll();
        }


        if(estados.isEmpty()){
            return new ArrayList<>();
        }

        Collection<DTOABMEstadoOrdenCompra> dto = new ArrayList<>();

        for(EstadoOrdenCompra estado: estados){
            if(estado.getFhBajaEstadoOrdenCompra() == null){
                dadoDeBaja = false;
            }else{
                dadoDeBaja = true;
            }

            DTOABMEstadoOrdenCompra aux = DTOABMEstadoOrdenCompra.builder()
                    .idEOC(estado.getId())
                    .fhBajaEOC(estado.getFhBajaEstadoOrdenCompra())
                    .nombreEstado(estado.getNombreEstadoOrdenCompra())
                    .dadoBaja(dadoDeBaja)
                    .build();

            dto.add(aux);
        }

        return dto;
    }

    public void altaEstado(String nombreEstado) {

        Collection<EstadoOrdenCompra> estados = repositorioEstadoOrdenCompra.findAll();

        for(EstadoOrdenCompra estado: estados){

            if(estado.getNombreEstadoOrdenCompra().equals(nombreEstado) && estado.getFhBajaEstadoOrdenCompra()==null){
                throw new CustomException("Error, el nombre ingresado ya se encuentra en uso");
            }
        }

        EstadoOrdenCompra estadoNuevo = EstadoOrdenCompra.builder()
                .nombreEstadoOrdenCompra(nombreEstado)
                .build();

        repositorioEstadoOrdenCompra.save(estadoNuevo);
    }

    public void bajaEstado(Long idEstadoOrdenCompra) {

        Optional<EstadoOrdenCompra> estado = repositorioEstadoOrdenCompra.findById(idEstadoOrdenCompra);

        if(estado.get().getFhBajaEstadoOrdenCompra()!=null){
            throw new CustomException("Error, el estado ya se encuentra dado de baja");
        }

        Collection<OrdenCompra> ordenesCompra = repositorioOrdenCompra.getOrdenesPorEstado(idEstadoOrdenCompra);

        if(!(ordenesCompra.isEmpty())){
            throw new CustomException("Error, el estado no se puede dar de baja porque tiene una orden de compra asociada");
        }

        estado.get().setFhBajaEstadoOrdenCompra(new Date());

        repositorioEstadoOrdenCompra.save(estado.get());

    }

    public DTOABMEstadoOrdenCompra getDatosEstado(Long idEstadoOrdenCompra){

        Optional<EstadoOrdenCompra> estado = repositorioEstadoOrdenCompra.findById(idEstadoOrdenCompra);

        if(estado.isEmpty()){
            throw new CustomException("Error, el estado seleccionado no existe");
        }

        DTOABMEstadoOrdenCompra dto = DTOABMEstadoOrdenCompra.builder()
                .idEOC(estado.get().getId())
                .nombreEstado(estado.get().getNombreEstadoOrdenCompra())
                .build();

        return dto;
    }

    public void confirmar(DTOABMEstadoOrdenCompra dto) {

        Collection<EstadoOrdenCompra> estados = repositorioEstadoOrdenCompra.findAll();

        for(EstadoOrdenCompra estado: estados){
            if(estado.getNombreEstadoOrdenCompra().equals(dto.getNombreEstado()) && estado.getFhBajaEstadoOrdenCompra()==null){
                throw new CustomException("Error, el nombre ingresado ya se encuentra en uso");
            }
        }

        Optional<EstadoOrdenCompra> estadoModificado = repositorioEstadoOrdenCompra.findById(dto.getIdEOC());

        estadoModificado.get().setNombreEstadoOrdenCompra(dto.getNombreEstado());

        repositorioEstadoOrdenCompra.save(estadoModificado.get());
    }

    @Transactional
    public void modificar(DTOModificarEstado dto) {

        EstadoOrdenCompra estadoViejo = repositorioEstadoOrdenCompra.obtenerEstadoVigentePorID(dto.getId()).orElseThrow(() -> new CustomException("No existe un estado vigente con ese id"));

        if (dto.getNuevoNombre().trim().isEmpty()) {
            throw new CustomException("No puede ponerse un nuevo nombre vacío");
        }

        if (repositorioEstadoOrdenCompra.findByNombreEstadoOrdenCompraAndFhBajaEstadoOrdenCompraIsNull(dto.getNuevoNombre()) != null){
            throw new CustomException("Ya existe un estado con ese nombre");
        }

        estadoViejo.setNombreEstadoOrdenCompra(dto.getNuevoNombre());

        repositorioEstadoOrdenCompra.save(estadoViejo);

    }

}
