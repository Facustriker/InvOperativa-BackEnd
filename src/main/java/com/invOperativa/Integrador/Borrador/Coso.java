package com.invOperativa.Integrador.Borrador;

import com.invOperativa.Integrador.Entidades.BaseEntity;
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
@Table(name = "Coso")
public class Coso extends BaseEntity {

    @Column(name = "fhaArticulo")
    private Date fhaArticulo;


    @Column(name = "nombreArticulo", nullable = false)
    private String nombreArticulo;
}
