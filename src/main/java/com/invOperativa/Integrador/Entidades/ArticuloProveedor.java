    package com.invOperativa.Integrador.Entidades;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Repositorios.RepositorioOrdenCompraDetalle;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ArticuloProveedor")
public class ArticuloProveedor extends BaseEntity{

    @Column(name = "costoPedido")
    private float costoPedido;

    @Column(name = "fhAsignacion", nullable = false)
    private Date fhAsignacion;

    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @Column(name = "demoraEntrega")
    private int demoraEntrega;

    @Column(name = "isPredeterminado", nullable = false)
    private boolean isPredeterminado;

    @Column(name = "CostoUnitario")
    private float costoUnitario;

    @Column(name = "nivelServicio")
    private float nivelServicio;

    @Column(name = "stockSeguridad")
    private Integer stockSeguridad;

    @Column(name = "loteOptimo")
    private Integer loteOptimo;

    @ManyToOne
    @JoinColumn(name = "articulo")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "modeloInventario")
    private ModeloInventario modeloInventario;

    public static double getZ(double nivelServicio) {
        if (nivelServicio <= 0 || nivelServicio >= 1) {
            throw new CustomException("El nivel de servicio usado NO se encuentra entre 0 y 1");
        }

        // Coeficientes
        double[] a = { -3.969683028665376e+01, 2.209460984245205e+02,
                -2.759285104469687e+02, 1.383577518672690e+02,
                -3.066479806614716e+01, 2.506628277459239e+00 };

        double[] b = { -5.447609879822406e+01, 1.615858368580409e+02,
                -1.556989798598866e+02, 6.680131188771972e+01,
                -1.328068155288572e+01 };

        double[] c = { -7.784894002430293e-03, -3.223964580411365e-01,
                -2.400758277161838e+00, -2.549732539343734e+00,
                4.374664141464968e+00, 2.938163982698783e+00 };

        double[] d = { 7.784695709041462e-03, 3.224671290700398e-01,
                2.445134137142996e+00, 3.754408661907416e+00 };

        double q, r;
        if (nivelServicio < 0.02425) {
            q = Math.sqrt(-2 * Math.log(nivelServicio));
            return (((((c[0]*q + c[1])*q + c[2])*q + c[3])*q + c[4])*q + c[5]) /
                    ((((d[0]*q + d[1])*q + d[2])*q + d[3])*q + 1);
        } else if (nivelServicio > 1 - 0.02425) {
            q = Math.sqrt(-2 * Math.log(1 - nivelServicio));
            return -(((((c[0]*q + c[1])*q + c[2])*q + c[3])*q + c[4])*q + c[5]) /
                    ((((d[0]*q + d[1])*q + d[2])*q + d[3])*q + 1);
        } else {
            q = nivelServicio - 0.5;
            r = q * q;
            return (((((a[0]*r + a[1])*r + a[2])*r + a[3])*r + a[4])*r + a[5])*q /
                    (((((b[0]*r + b[1])*r + b[2])*r + b[3])*r + b[4])*r + 1);
        }
    }

    public static float getSigma(float d,int T, int L){
        float i = 0.25F;

        return (float) (Math.sqrt(T+L)*d*i);
    }

    public int getCantidadTiempoFijo(RepositorioOrdenCompraDetalle repositorioOrdenCompraDetalle){

        int demanda = articulo.getDemanda()/365;
        int tiempoFijo = articulo.getTiempoFijo();
        float z = (float) getZ(nivelServicio);
        float sigma = getSigma(demanda , tiempoFijo, demoraEntrega);
        int stockActual = articulo.getStock();
        Integer stockPedido = repositorioOrdenCompraDetalle.obtenerCantidadTotalDeArticuloEnviado(articulo.getId());

        if (stockPedido == null){
            stockPedido = 0;
        }

        float primerTermino = demanda * (tiempoFijo + demoraEntrega);
        float segundoTermino = z * sigma - (stockActual + stockPedido);

        return (int) (primerTermino + segundoTermino);

    }
}
