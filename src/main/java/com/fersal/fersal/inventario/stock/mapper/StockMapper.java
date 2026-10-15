package com.fersal.fersal.inventario.stock.mapper;


import com.fersal.fersal.inventario.stock.dto.StockResponse;
import com.fersal.fersal.inventario.stock.entity.Stock;


import org.springframework.stereotype.Component;


@Component
public class StockMapper {


    public StockResponse toResponse(
            Stock s
    ) {


        return StockResponse.builder()

                .id(
                        s.getId()
                )

                .empresaId(
                        s.getEmpresa().getId()
                )

                .sucursalId(
                        s.getSucursal().getId()
                )

                .sucursalNombre(
                        s.getSucursal().getNombre()
                )

                .productoId(
                        s.getProducto().getId()
                )

                .productoNombre(
                        s.getProducto().getNombre()
                )

                .cantidad(
                        s.getCantidad()
                )

                .stockMinimo(
                        s.getStockMinimo()
                )

                .build();

    }


}