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
@Table(name = "Proveedor")
public class Proveedor extends BaseEntity{

    @Column(name = "fhBajaProveedor")
    private Date fhBajaProveedor;


    @Column(name = "nombreProveedor")
    private String nombreProveedor;
}
