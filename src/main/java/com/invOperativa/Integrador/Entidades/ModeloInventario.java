package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ModeloInventario")
public class ModeloInventario extends BaseEntity{

//ID del modelo de inventario lo obtiene de BaseEntity

    @Column(name = "nombreModelo")
    private String nombreModelo;

    @Column(name = "fhBajaModeloInventario")
    private Date fhBajaModeloInventario;
    
}
