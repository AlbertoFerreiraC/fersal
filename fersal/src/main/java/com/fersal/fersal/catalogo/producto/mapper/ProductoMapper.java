package com.fersal.fersal.catalogo.producto.mapper;

import com.fersal.fersal.catalogo.producto.dto.ProductoResponse;
import com.fersal.fersal.catalogo.producto.entity.Producto;

import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoResponse toResponse(Producto p) {

        return ProductoResponse.builder()

                .id(p.getId())

                .empresaId(
                        p.getEmpresa().getId()
                )

                .empresaNombre(
                        p.getEmpresa().getNombre()
                )

                .categoriaId(
                        p.getCategoria() != null
                                ? p.getCategoria().getId()
                                : null
                )

                .categoriaNombre(
                        p.getCategoria() != null
                                ? p.getCategoria().getNombre()
                                : null
                )

                .proveedorId(
                        p.getProveedor() != null
                                ? p.getProveedor().getId()
                                : null
                )

                .proveedorNombre(
                        p.getProveedor() != null
                                ? p.getProveedor().getNombre()
                                : null
                )

                .codigo(p.getCodigo())

                .nombre(p.getNombre())

                .descripcion(p.getDescripcion())

                .imagenUrl(p.getImagenUrl())

                .precioCompra(p.getPrecioCompra())

                .precioVenta(p.getPrecioVenta())

                .stockMinimo(p.getStockMinimo())

                .activo(p.getActivo())

                .build();
    }
}