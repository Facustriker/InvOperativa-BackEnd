package com.invOperativa.Integrador.CU.CU17_ABMModeloInventario;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.ModeloInventario;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioModeloInventario;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoABMModeloInventario {

    @Autowired
    private final RepositorioModeloInventario repositorioModeloInventario;

    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor; 



// GET (difiere del get del modificar en que este es para uso interno del abm, el otro es para el usuario)

    // PARA CONSULTAR MODELOS INVENTARIO (VIGENTES O TODOS)
    public Collection<DTOABMModeloInventario> getModelos(boolean soloVigentes) {//parámetro en el que me indican si quieren solo lso vigentes o todos 
        boolean dadoDeBaja;
        
        Collection<ModeloInventario> modelos;

        if(soloVigentes){
            modelos = repositorioModeloInventario.getModelosVigentes();
        }else{
            modelos = repositorioModeloInventario.findAll();
        }

        //utilizo la variable modelo que acabo de llenar con data traida de la base de datos
        if(modelos.isEmpty()){
            return new ArrayList<>();
        }



    // Ahora para msotrar en el Front lo qué encontré, tengo que crear un DTO para envíarle al controlador
        
        //1 creo el array que va a ir guardando lso dtos
        Collection<DTOABMModeloInventario> dtoMI = new ArrayList<>();

        //2 recorro el array de modelos (modelos es el array que llene con los datos de la base de datos en la linea 33)
        for(ModeloInventario modelo: modelos){
            //2.0 verifico si el modelo está dado de baja (por si pedí traer todos, no solo los vigentes)   
            if(modelo.getFhBajaModeloInventario() == null){
                dadoDeBaja = false;
            }else{
                dadoDeBaja = true;
            }
            //2.1 para cada modelo (almacenado en la variable modelo) que encontré, lo convierto a DTO
            DTOABMModeloInventario auxMI = DTOABMModeloInventario.builder()
                    .idMI(modelo.getId())
                    .fhBajaMI(modelo.getFhBajaModeloInventario())
                    .nombreMI(modelo.getNombreModelo())
                    .MIdadoBaja(dadoDeBaja)
                    .build();

            //2.2 lo agrego al array de dtos
            dtoMI.add(auxMI);
        }

        return dtoMI; // DEVUELVO UNA COLLECCION DE DTOs
    }


// ALTA

    public void altaModelo(String nombreModelo) {

        //1 Busco todos los modelos en la base de datos
        Collection<ModeloInventario> modelosBD = repositorioModeloInventario.findAll();

        //2 Recorro todos los modelos en la base de datos
        for(ModeloInventario modelo: modelosBD){

            if(modelo.getNombreModelo().equals(nombreModelo) && modelo.getFhBajaModeloInventario()==null){
                throw new CustomException("Error, ya existe un modelo con ese nombre");
            }
        }

        //3 Si no hubo error, creo el nuevo modelo (Solo pido nombre proque el id se autogenera)
        ModeloInventario modeloNuevo = ModeloInventario.builder()
                .nombreModelo(nombreModelo)
                .build();

        repositorioModeloInventario.save(modeloNuevo); //guardo el nuevo modelo en la base de datos
    }


// BAJA

    public void bajaModelo(Long idModeloInventario) {

        //El uso de Optional es para evitar NullPointerException.
        Optional<ModeloInventario> modelo = repositorioModeloInventario.findById(idModeloInventario);

        if(modelo.get().getFhBajaModeloInventario()!=null){
            throw new CustomException("Error, el modelo ya se encuentra dado de baja");
        }

        //Me traigo todos lso artículso que tengan este modelo de inventario asociado
        Collection<ArticuloProveedor> articulosProveedores = repositorioArticuloProveedor.getAPPorModeloInventario(idModeloInventario);

        //si tiene artículos asociados, no lo doy de baja
        if(!(articulosProveedores.isEmpty())){
            throw new CustomException("Error, el modelo no se puede dar de baja porque tiene artículos asociados");
        }
        //si no tiene artículos asociados, lo doy de baja
        modelo.get().setFhBajaModeloInventario(new Date());
        
        //Actualizo la base de datos
        repositorioModeloInventario.save(modelo.get()); //acá uso el get y no pongo modelo solo porque modelo es un optional, no un objeto de ModeloInventario

    }


//MODIFICACIÓN

    //GET y mostrar en front
    public DTOABMModeloInventario getDatosModelo(Long idModeloInventario){

        Optional<ModeloInventario> modeloInventario = repositorioModeloInventario.findById(idModeloInventario);

        if(modeloInventario.isEmpty()){
            throw new CustomException("Error, el Modelo Inventario seleccionado no existe");
        }

        //1.1 Creo el DTO para mostrar los datos del modelo de inventario
        DTOABMModeloInventario dtoMiMod = DTOABMModeloInventario.builder()
                .idMI(modeloInventario.get().getId())
                .nombreMI(modeloInventario.get().getNombreModelo())
                .fhBajaMI(modeloInventario.get().getFhBajaModeloInventario())
                .MIdadoBaja(modeloInventario.get().getFhBajaModeloInventario() != null)
                .build();

        return dtoMiMod;
    }

    //Guardar modificación
    //eNTRA EL DTO QUE VIENE DEL FRONT
    public void modificarModelo(DTOABMModeloInventario dto) {

        //Reviso que el nombre no esté en uso
        Collection<ModeloInventario> modelosInventariobd = repositorioModeloInventario.findAll();

        for(ModeloInventario modeloInventario: modelosInventariobd){
            if(modeloInventario.getNombreModelo().equals(dto.getNombreMI()) && modeloInventario.getFhBajaModeloInventario()==null){
                throw new CustomException("Error, el nombre ingresado ya se encuentra en uso");
            } 
            
        }
        
            //Busco en la BD el modeloInventarioe específico que me indicó el usuario en el dto
        Optional<ModeloInventario> modeloInventarioModificado = repositorioModeloInventario.findById(dto.getIdMI());

           //Reviso que el modelo no esté dado de baja          
        if (modeloInventarioModificado.get().getFhBajaModeloInventario() != null) {
            throw new CustomException("Error, no se puede modificar un modelo dado de baja");
        }

        //Modifico el nombre
        modeloInventarioModificado.get().setNombreModelo(dto.getNombreMI());

        //Actualizo la base de datos
        repositorioModeloInventario.save(modeloInventarioModificado.get());
    }

}
