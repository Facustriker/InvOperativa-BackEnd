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

    @Column(name = "fhaArticulo")
    private Date fhaArticulo;


    @Column(name = "nombreArticulo", nullable = false)
    private String nombreArticulo;
}
