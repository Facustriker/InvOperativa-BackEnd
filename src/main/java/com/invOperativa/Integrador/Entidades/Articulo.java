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

    @Column(name = "costoAlmacenamiento", nullable = false)
    private float costoAlmacenamiento;

    @Column(name = "demanda", nullable = false)
    private int demanda;

    @Column(name = "descripcionArt", nullable = false)
    private String descripcionArt;

    @Column(name = "fhBajaArticulo")
    private Date fhBajaArticulo;

    @Column(name = "inventarioMaxArticulo")
    private int inventarioMaxArticulo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precioUnitario", nullable = false)
    private float precioUnitario;

    @Column(name = "proximaRevision")
    private Date proximaRevision;

    @Column(name = "puntoPedido")
    private Integer puntoPedido;

    @Column(name = "stock")
    private int stock;

    @Column(name = "tiempoFijo")
    private Integer tiempoFijo;

    public void calcularProximaRevision() {
        if (this.tiempoFijo != null && this.tiempoFijo > 0) {
            long milisegundosPorDia = 24L * 60L * 60L * 1000L;
            Date nuevaFecha = new Date(System.currentTimeMillis() + this.tiempoFijo * milisegundosPorDia);
            this.proximaRevision = nuevaFecha;
        }
    }

}
