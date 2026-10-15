package com.fersal.fersal.dashboard.service;

import com.fersal.fersal.cliente.cuenta.repository.CuentaCorrienteRepository;
import com.fersal.fersal.compra.compra.repository.CuentaPorPagarRepository;
import com.fersal.fersal.dashboard.dto.DashboardResponse;
import com.fersal.fersal.inventario.stock.repository.StockRepository;
import com.fersal.fersal.venta.repository.DetalleVentaRepository;
import com.fersal.fersal.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final CuentaPorPagarRepository cuentaPorPagarRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerMetricas(UUID empresaId, UUID sucursalId) {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);

        // 1. Ventas del Día
        BigDecimal ventasHoy = ventaRepository.sumTotalVentasHoy(empresaId, sucursalId, inicioDia, finDia);
        if (ventasHoy == null) ventasHoy = BigDecimal.ZERO;

        Long transaccionesHoy = ventaRepository.countVentasHoy(empresaId, sucursalId, inicioDia, finDia);
        if (transaccionesHoy == null) transaccionesHoy = 0L;

        // Costo de compra de los productos vendidos hoy para margen bruto
        BigDecimal costoHoy = detalleVentaRepository.sumCostoProductosVendidosHoy(empresaId, sucursalId, inicioDia, finDia);
        if (costoHoy == null) costoHoy = BigDecimal.ZERO;
        BigDecimal gananciaHoy = ventasHoy.subtract(costoHoy);
        if (gananciaHoy.compareTo(BigDecimal.ZERO) < 0) gananciaHoy = BigDecimal.ZERO;

        // 2. Balances Financieros (Fiados y Cuentas por Pagar activos de la empresa)
        BigDecimal totalPorCobrar = cuentaCorrienteRepository.sumSaldoPendiente(empresaId);
        if (totalPorCobrar == null) totalPorCobrar = BigDecimal.ZERO;

        BigDecimal totalPorPagar = cuentaPorPagarRepository.sumSaldoPendienteEmpresa(empresaId);
        if (totalPorPagar == null) totalPorPagar = BigDecimal.ZERO;

        // 3. Stock Bajo real desde la base de datos
        List<DashboardResponse.StockBajoItem> stockBajo = stockRepository
                .findStockBajo(empresaId, sucursalId, PageRequest.of(0, 5))
                .stream()
                .map(s -> DashboardResponse.StockBajoItem.builder()
                        .productoId(s.getProducto().getId())
                        .nombre(s.getProducto().getNombre())
                        .stockActual(s.getCantidad())
                        .stockMinimo(s.getStockMinimo())
                        .build())
                .toList();

        // 4. Últimas 5 transacciones de ventas
        List<DashboardResponse.TransaccionRecienteItem> transacciones = ventaRepository
                .findTop5ByEmpresaIdAndSucursalIdOrderByFechaDesc(empresaId, sucursalId)
                .stream()
                .map(v -> DashboardResponse.TransaccionRecienteItem.builder()
                        .ventaId(v.getId())
                        .numero(v.getNumero())
                        .cliente(v.getCliente() != null
                                ? v.getCliente().getNombre() + " " + (v.getCliente().getApellido() != null ? v.getCliente().getApellido() : "")
                                : "Consumidor Final")
                        .total(v.getTotal())
                        .tipoPago(v.getTipoPago())
                        .fecha(v.getFecha())
                        .build())
                .toList();

        return DashboardResponse.builder()
                .ventasHoy(ventasHoy)
                .transaccionesHoy(transaccionesHoy)
                .gananciaHoy(gananciaHoy)
                .totalPorCobrar(totalPorCobrar)
                .totalPorPagar(totalPorPagar)
                .stockBajo(stockBajo)
                .ultimasTransacciones(transacciones)
                .build();
    }
}