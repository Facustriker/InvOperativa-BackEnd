package com.invOperativa.Integrador;

import com.invOperativa.Integrador.Entidades.*;
import com.invOperativa.Integrador.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@SpringBootApplication
public class ProyectoIntegradorApplication {

	//Los repositorios y el Bean son para hacer una carga inicial de datos, si quieren hacerlo
	//solo sáquenle el comentario y ejecuten, mientras estén comentados no hacen nada

	@Autowired
	private RepositorioArticuloProveedor repositorioArticuloProveedor;

	@Autowired
	private RepositorioArticulo repositorioArticulo;

	@Autowired
	private RepositorioProveedor repositorioProveedor;

	@Autowired
	private RepositorioModeloInventario repositorioModeloInventario;

	@Autowired
	private RepositorioOrdenCompra repositorioOrdenCompra;

	@Autowired
	private RepositorioEstadoOrdenCompra repositorioEstadoOrdenCompra;

	public static void main(String[] args) {
		SpringApplication.run(ProyectoIntegradorApplication.class, args);
		System.out.println("El proyecto está funcionando");
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {

			/*

			ModeloInventario modeloLoteFijo = ModeloInventario.builder()
					.nombreModelo("Lote fijo")
					.build();

			ModeloInventario modeloTiempoFijo = ModeloInventario.builder()
					.nombreModelo("Tiempo fijo")
					.build();

			modeloLoteFijo = repositorioModeloInventario.save(modeloLoteFijo);
			modeloTiempoFijo = repositorioModeloInventario.save(modeloTiempoFijo);

			Proveedor pr1 = Proveedor.builder()
					.nombreProveedor("Martin")
					.build();

			Proveedor pr2 = Proveedor.builder()
					.nombreProveedor("Lucas")
					.build();

			Proveedor pr3 = Proveedor.builder()
					.nombreProveedor("Pablo")
					.build();

			pr1 = repositorioProveedor.save(pr1);
			pr2 = repositorioProveedor.save(pr2);
			pr3 = repositorioProveedor.save(pr3);

			Articulo art1 = Articulo.builder()
					.nombre("Shampoo Plusbelle")
					.costoAlmacenamiento(30F)
					.demanda(400)
					.descripcionArt("Shampoo de calidad")
					.inventarioMaxArticulo(12450)
					.precioUnitario(1250.30F)
					.proximaRevision(null)
					.puntoPedido(987)
					.stock(4000)
					.tiempoFijo(14)
					.build();

			Articulo art2 = Articulo.builder()
					.nombre("Shampoo Axion")
					.costoAlmacenamiento(22F)
					.demanda(555)
					.descripcionArt("Shampoo de calidad mejor")
					.inventarioMaxArticulo(12950)
					.precioUnitario(2250.30F)
					.proximaRevision(null)
					.puntoPedido(900)
					.stock(100)
					.tiempoFijo(25)
					.build();

			Articulo art3 = Articulo.builder()
					.nombre("Shampoo Lavandidna")
					.costoAlmacenamiento(10F)
					.demanda(100)
					.descripcionArt("Shampoo de incalidad")
					.inventarioMaxArticulo(5550)
					.precioUnitario(750.30F)
					.proximaRevision(null)
					.puntoPedido(3000)
					.stock(2000)
					.tiempoFijo(7)
					.build();

			Articulo art4 = Articulo.builder()
					.nombre("Play5")
					.costoAlmacenamiento(300F)
					.demanda(2000)
					.descripcionArt("Consola berreta")
					.inventarioMaxArticulo(6250)
					.precioUnitario(10250.30F)
					.proximaRevision(null)
					.puntoPedido(2000)
					.stock(200)
					.tiempoFijo(2)
					.build();

			art1 = repositorioArticulo.save(art1);
			art2 = repositorioArticulo.save(art2);
			art3 = repositorioArticulo.save(art3);
			art4 = repositorioArticulo.save(art4);

			ArticuloProveedor artProv1 = ArticuloProveedor.builder()
					.costoPedido(500F)
					.costoUnitario(250F)
					.demoraEntrega(12)
					.fhAsignacion(new Date())
					.isPredeterminado(true)
					.loteOptimo(650)
					.nivelServicio(0.95F)
					.stockSeguridad(200)
					.proveedor(pr1)
					.articulo(art1)
					.modeloInventario(modeloLoteFijo)
					.build();

			ArticuloProveedor artProv2 = ArticuloProveedor.builder()
					.costoPedido(700F)
					.costoUnitario(1000F)
					.demoraEntrega(5)
					.fhAsignacion(new Date())
					.isPredeterminado(false)
					.loteOptimo(1200)
					.nivelServicio(0.90F)
					.stockSeguridad(50)
					.proveedor(pr2)
					.articulo(art1)
					.modeloInventario(modeloTiempoFijo)
					.build();

			ArticuloProveedor artProv3 = ArticuloProveedor.builder()
					.costoPedido(1200F)
					.costoUnitario(350F)
					.demoraEntrega(17)
					.fhAsignacion(new Date())
					.isPredeterminado(true)
					.loteOptimo(690)
					.nivelServicio(0.98F)
					.stockSeguridad(120)
					.proveedor(pr2)
					.articulo(art2)
					.modeloInventario(modeloLoteFijo)
					.build();

			ArticuloProveedor artProv4 = ArticuloProveedor.builder()
					.costoPedido(4000F)
					.costoUnitario(20F)
					.demoraEntrega(3)
					.fhAsignacion(new Date())
					.isPredeterminado(true)
					.loteOptimo(45)
					.nivelServicio(0.88F)
					.stockSeguridad(10)
					.proveedor(pr3)
					.articulo(art3)
					.modeloInventario(modeloTiempoFijo)
					.build();

			ArticuloProveedor artProv5 = ArticuloProveedor.builder()
					.costoPedido(350F)
					.costoUnitario(19000F)
					.demoraEntrega(30)
					.fhAsignacion(new Date())
					.isPredeterminado(false)
					.loteOptimo(444)
					.nivelServicio(0.86F)
					.stockSeguridad(300)
					.proveedor(pr3)
					.articulo(art4)
					.modeloInventario(modeloLoteFijo)
					.build();

			ArticuloProveedor artProv6 = ArticuloProveedor.builder()
					.costoPedido(1200F)
					.costoUnitario(4500F)
					.demoraEntrega(1)
					.fhAsignacion(new Date())
					.isPredeterminado(true)
					.loteOptimo(999)
					.nivelServicio(0.99F)
					.stockSeguridad(400)
					.proveedor(pr2)
					.articulo(art4)
					.modeloInventario(modeloTiempoFijo)
					.build();

			artProv1 = repositorioArticuloProveedor.save(artProv1);
			artProv2 = repositorioArticuloProveedor.save(artProv2);
			artProv3 = repositorioArticuloProveedor.save(artProv3);
			artProv4 = repositorioArticuloProveedor.save(artProv4);
			artProv5 = repositorioArticuloProveedor.save(artProv5);
			artProv6 = repositorioArticuloProveedor.save(artProv6);

			EstadoOrdenCompra estado1 = EstadoOrdenCompra.builder()
					.nombreEstadoOrdenCompra("Pendiente")
					.build();

			EstadoOrdenCompra estado2 = EstadoOrdenCompra.builder()
					.nombreEstadoOrdenCompra("Finalizada")
					.build();

			EstadoOrdenCompra estado3 = EstadoOrdenCompra.builder()
					.nombreEstadoOrdenCompra("Enviada")
					.build();

			estado1 = repositorioEstadoOrdenCompra.save(estado1);
			estado2 = repositorioEstadoOrdenCompra.save(estado2);
			estado3 = repositorioEstadoOrdenCompra.save(estado3);

			OrdenCompraDetalle ocd1 = OrdenCompraDetalle.builder()
					.cantidad(5000)
					.subTotal(7350)
					.articuloProveedor(artProv2)
					.build();

			OrdenCompraDetalle ocd2 = OrdenCompraDetalle.builder()
					.cantidad(3000)
					.subTotal(4345)
					.articuloProveedor(artProv3)
					.build();

			OrdenCompraDetalle ocd3 = OrdenCompraDetalle.builder()
					.cantidad(8000)
					.subTotal(7456)
					.articuloProveedor(artProv6)
					.build();

			OrdenCompraDetalle ocd4 = OrdenCompraDetalle.builder()
					.cantidad(14000)
					.subTotal(8954)
					.articuloProveedor(artProv4)
					.build();

			OrdenCompraDetalle ocd5 = OrdenCompraDetalle.builder()
					.cantidad(10000)
					.subTotal(9812)
					.articuloProveedor(artProv2)
					.build();

			OrdenCompra oc1 = OrdenCompra.builder()
					.fhAltaOrdenCompra(new Date())
					.total(50000)
					.build();

			oc1.setEstadoOrdenCompra(estado1);
			oc1.addOrdenCompraDetalle(ocd1);
			oc1.addOrdenCompraDetalle(ocd2);
			oc1.addOrdenCompraDetalle(ocd3);

			OrdenCompra oc2 = OrdenCompra.builder()
					.fhAltaOrdenCompra(new Date())
					.total(35000)
					.build();

			oc2.setEstadoOrdenCompra(estado2);
			oc2.addOrdenCompraDetalle(ocd4);
			oc2.addOrdenCompraDetalle(ocd5);

			repositorioOrdenCompra.save(oc1);
			repositorioOrdenCompra.save(oc2);




			 */


			
		};
	}

}
