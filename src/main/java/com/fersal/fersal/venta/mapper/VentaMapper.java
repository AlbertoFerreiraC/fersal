package com.fersal.fersal.venta.mapper;


import com.fersal.fersal.venta.dto.DetalleVentaResponse;
import com.fersal.fersal.venta.dto.VentaResponse;
import com.fersal.fersal.venta.entity.Venta;
import org.springframework.stereotype.Component;

@Component
public class VentaMapper {


    public VentaResponse toResponse(Venta venta) {


        return VentaResponse.builder()

                .id(venta.getId())

                .numero(venta.getNumero())

                .cliente(venta.getCliente() != null ? venta.getCliente().getNombre() : "Consumidor Final")

                .documento(venta.getCliente() != null ? venta.getCliente().getDocumento() : null)

                .telefono(venta.getCliente() != null ? venta.getCliente().getTelefono() : null)

                .direccion(venta.getCliente() != null ? venta.getCliente().getDireccion() : null)

                .empresa(venta.getEmpresa().getNombre())

                .empresaRuc(venta.getEmpresa().getRuc())

                .empresaTelefono(venta.getEmpresa().getTelefono())

                .fecha(venta.getFecha().toString())

                .observacion(venta.getObservacion())

                .total(venta.getTotal())

                .estado(venta.getEstado())

                .tipoPago(venta.getTipoPago())

                .detalles(venta.getDetalles().stream().map(detalle -> DetalleVentaResponse.builder()

                        .productoId(detalle.getProducto().getId())

                        .producto(detalle.getProducto().getNombre())

                        .cantidad(detalle.getCantidad())

                        .precio(detalle.getPrecioUnitario())

                        .subtotal(detalle.getSubtotal())

                        .build()).toList())

                .motivoAnulacion(venta.getMotivoAnulacion())

                .fechaAnulacion(venta.getFechaAnulacion() != null ? venta.getFechaAnulacion().toString() : null)

                .build();

    }


}