package com.fersal.fersal.caja.cierre.mapper;

import com.fersal.fersal.caja.cierre.dto.CierreCajaResponse;
import com.fersal.fersal.caja.cierre.entity.CierreCaja;
import com.fersal.fersal.caja.cierre.entity.EstadoCaja;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CierreCajaMapper {

    public CierreCajaResponse toResponse(CierreCaja cierre) {
        if (cierre == null) {
            return null;
        }

        return CierreCajaResponse.builder()
                .id(cierre.getId())
                .empresaId(cierre.getEmpresa() != null ? cierre.getEmpresa().getId() : null)
                .sucursalId(cierre.getSucursal() != null ? cierre.getSucursal().getId() : null)
                .usuarioId(cierre.getUsuario() != null ? cierre.getUsuario().getId() : null)
                .saldoInicial(cierre.getSaldoInicial() != null ? cierre.getSaldoInicial() : BigDecimal.ZERO)
                .totalIngresos(cierre.getTotalIngresos() != null ? cierre.getTotalIngresos() : BigDecimal.ZERO)
                .totalEgresos(cierre.getTotalEgresos() != null ? cierre.getTotalEgresos() : BigDecimal.ZERO)
                .saldoFinal(cierre.getSaldoFinal() != null ? cierre.getSaldoFinal() : BigDecimal.ZERO)
                .estado(cierre.getEstado() != null ? cierre.getEstado() : EstadoCaja.CERRADA)
                .fechaApertura(cierre.getFechaApertura())
                .fechaCierre(cierre.getFechaCierre())
                .build();
    }
}