package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ModeloInventario") //Le indica que Esta entidad se volverá una tabla


public class ModeloInventario extends BaseEntity{

    // ya heredó el atributo id de tipo long de la entidad BaseEntity que está extendiendo

    @Column(name = "nombreModelo") //Indica a la persistencia que cree una columna en al tabla que contega:
    private String nombreModelo;
    @Column(name = "fhBajaModeloInventario")
    private Date fhBajaModeloInventario;
}
