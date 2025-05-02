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
@Table(name = "EstadoOrdenCompra")
public class EstadoOrdenCompra extends BaseEntity{

    @Column(name = "nombreEstadoOrdenCompra", nullable = false)
    private String nombreEstadoOrdenCompra;

    @Column(name = "fhBajaEstadoOrdenCompra")
    private Date fhBajaEstadoOrdenCompra;
}
