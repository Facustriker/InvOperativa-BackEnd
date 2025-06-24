package com.invOperativa.Integrador.CU.CU18_CalcularCGI;

import com.invOperativa.Integrador.Config.CustomException;
import com.invOperativa.Integrador.Entidades.ArticuloProveedor;
import com.invOperativa.Integrador.Repositorios.RepositorioArticuloProveedor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertoCalcularCGI {

    //Necesito saber el tipo de inventario que usa el proveedor del artículo
    //y ademas de acá saco los artículos
    //Necesito calcular el costo del artículo y el del pedido
    @Autowired
    private final RepositorioArticuloProveedor repositorioArticuloProveedor;

    public DTOCalcularCGI calculoCGI(Long idArticulo) {
        Collection<ArticuloProveedor> artProveedores = repositorioArticuloProveedor.getArticulosProveedorVigentesPorArticuloId(idArticulo);
        Optional<ArticuloProveedor> artProveedorAuxiliar = artProveedores.stream().findFirst();

        if (artProveedorAuxiliar.isEmpty()) {
            throw new CustomException("Error, el artículo no tiene un proveedor asignado");
        }

        String nombreArticulo = artProveedorAuxiliar.get().getArticulo().getNombre();

        // Filtramos para obtener el predeterminado
        Optional<ArticuloProveedor> predeterminado = artProveedores.stream()
                .filter(ArticuloProveedor::isPredeterminado)
                .findFirst();

        if (predeterminado.isEmpty()) {
            throw new CustomException("Error, no existe un proveedor marcado como predeterminado para este artículo");
        }

        ArticuloProveedor articuloProveedor = predeterminado.get();
        DTOCalcularCGI dtoCalcularCGI = DTOCalcularCGI.builder()
                .nombreArticulo(nombreArticulo)
                .build();

        // Ahora calculamos CGI SOLO para este proveedor
        if ("Lote fijo".equals(articuloProveedor.getModeloInventario().getNombreModelo())) {
            float CGI;
            float costoUnitario = articuloProveedor.getCostoUnitario();
            int demanda = articuloProveedor.getArticulo().getDemanda();
            float costo = articuloProveedor.getCostoPedido();
            int loteOptimo = articuloProveedor.getLoteOptimo();
            float costoAlmacenamiento = articuloProveedor.getArticulo().getCostoAlmacenamiento();

            float CC = costoUnitario * demanda;
            float CP = (float) demanda / loteOptimo * costo;
            float CA = (float) loteOptimo / 2 * costoAlmacenamiento;

            CGI = CC + CP + CA;

            DTODatosCGI aux = DTODatosCGI.builder()
                    .nombreProveedor(articuloProveedor.getProveedor().getNombreProveedor())
                    .nombreTipoModelo(articuloProveedor.getModeloInventario().getNombreModelo())
                    .CGI(CGI)
                    .costoCompra(CC)
                    .costoPedido(CP)
                    .costoAlmacenamiento(CA)
                    .isPredeterminado(true)
                    .build();

            dtoCalcularCGI.addDato(aux);

        } else {
            float CGI;
            float costoUnitario = articuloProveedor.getCostoUnitario();
            int demanda = articuloProveedor.getArticulo().getDemanda();
            float costo = articuloProveedor.getCostoPedido();
            float costoAlmacenamiento = articuloProveedor.getArticulo().getCostoAlmacenamiento();
            Integer tiempoFijo = articuloProveedor.getArticulo().getTiempoFijo();
            int demoraProveedor = articuloProveedor.getDemoraEntrega();
            int stockArticulo = articuloProveedor.getArticulo().getStock();
            float z = (float) getZ(articuloProveedor.getNivelServicio());
            float d = (float) demanda / 365;
            float stockSeguridad = articuloProveedor.getStockSeguridad();
            float sigmaTL = getSigma(d, tiempoFijo, demoraProveedor);

            float q = (d) * (tiempoFijo + demoraProveedor) + (z * sigmaTL) - (stockArticulo);

            float CC = costoUnitario * demanda;
            float CP = demanda / q * costo;
            float CA = q / 2 + stockSeguridad * costoAlmacenamiento;

            CGI = CC + CP + CA;

            DTODatosCGI aux = DTODatosCGI.builder()
                    .nombreProveedor(articuloProveedor.getProveedor().getNombreProveedor())
                    .nombreTipoModelo(articuloProveedor.getModeloInventario().getNombreModelo())
                    .CGI(CGI)
                    .costoCompra(CC)
                    .costoPedido(CP)
                    .costoAlmacenamiento(CA)
                    .isPredeterminado(true)
                    .build();

            dtoCalcularCGI.addDato(aux);
        }

        return dtoCalcularCGI;
    }

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

}
