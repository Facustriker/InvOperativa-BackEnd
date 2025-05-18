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
@Table(name = "ModeloInventario")
public class ModeloInventario extends BaseEntity{

    @Column(name = "nombreModelo")
    private String nombreModelo;
}
