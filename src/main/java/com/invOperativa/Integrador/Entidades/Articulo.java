package com.invOperativa.Integrador.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Articulo")
public class Articulo extends BaseEntity{

    @Column(name = "descripcionArt", nullable = false)
    private String descripcionArt;

    @Column(name = "precioUnitario", nullable = false)
    private float precioUnitario;

    @Column(name = "costoAlmacenamiento", nullable = false)
    private float costoAlmacenamiento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fhBajaArticulo")
    private Date fhBajaArticulo;
}
