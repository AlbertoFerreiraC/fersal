package com.fersal.fersal.venta.comprobante.service;

import com.fersal.fersal.venta.comprobante.dto.ComprobanteResponse;
import com.fersal.fersal.venta.entity.Venta;
import com.fersal.fersal.venta.repository.VentaRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComprobanteServiceImpl implements ComprobanteService {

    private static final String RUTA_LOGO_LOCAL = "C:\\Users\\Alberto Ferreira\\Desktop\\treinta\\fersal\\uploads\\Logo\\fersal_logo_letras.png";

    // Colores Deuda / Pendiente (Rojo / Rosa)
    private static final Color COLOR_ROJO_TEXTO = new Color(180, 40, 45);
    private static final Color COLOR_ROSA_BG = new Color(252, 232, 230);

    // Colores Pagado / Contado (Verde)
    private static final Color COLOR_VERDE_TEXTO = new Color(21, 128, 61);
    private static final Color COLOR_VERDE_BG = new Color(220, 252, 231);

    // Colores Neutros
    private static final Color COLOR_GRIS_BG = new Color(240, 242, 245);
    private static final Color COLOR_TEXTO_OSCURO = new Color(30, 30, 30);

    private final VentaRepository ventaRepository;

    @Override
    public ComprobanteResponse generar(UUID ventaId) {
        Venta venta = ventaRepository.findById(ventaId).orElseThrow();
        byte[] pdf = generarPdf(venta);

        return ComprobanteResponse.builder()
                .nombreArchivo("ticket-" + venta.getNumero() + ".pdf")
                .contenidoBase64(Base64.getEncoder().encodeToString(pdf))
                .build();
    }

    private byte[] generarPdf(Venta venta) {
        // Hacemos dos pasadas: la primera calcula la altura real y la segunda genera el PDF recortado.
        float alturaContenido = calcularAlturaReal(venta);

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // Alto dinámico exacto según el contenido medido (+15pt de margen inferior)
            float altoFinal = Math.max(200, alturaContenido + 15);
            Rectangle ticket = new Rectangle(230, altoFinal);
            Document document = new Document(ticket, 10, 10, 10, 10);

            PdfWriter.getInstance(document, output);
            document.open();

            construirContenidoPdf(document, venta);

            document.close();
            return output.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando ticket", e);
        }
    }

    private float calcularAlturaReal(Venta venta) {
        try {
            ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
            // Creamos un lienzo temporal alto para permitir que todo el texto renderice
            Rectangle tempTicket = new Rectangle(230, 2000);
            Document tempDoc = new Document(tempTicket, 10, 10, 10, 10);

            PdfWriter writer = PdfWriter.getInstance(tempDoc, tempOut);
            tempDoc.open();

            construirContenidoPdf(tempDoc, venta);

            // Obtenemos la posición Y donde terminó el último elemento
            float ultimaPosicionY = writer.getVerticalPosition(true);
            tempDoc.close();

            // Altura consumida = Alto Inicial (2000) - Posición Y Final
            return 2000 - ultimaPosicionY;
        } catch (Exception e) {
            return 600; // Valor fallback por defecto si falla el cálculo
        }
    }

    private void construirContenidoPdf(Document document, Venta venta) throws DocumentException {
        // Lógica de Deuda vs Pagado
        boolean esDeuda = esCuentaPendiente(venta);

        Color colorEstadoTexto = esDeuda ? COLOR_ROJO_TEXTO : COLOR_VERDE_TEXTO;
        Color colorEstadoBg = esDeuda ? COLOR_ROSA_BG : COLOR_VERDE_BG;

        // Fuentes dinámicas
        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_TEXTO_OSCURO);
        Font fBannerTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, COLOR_TEXTO_OSCURO);
        Font fNumeroAccento = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, colorEstadoTexto);
        Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_TEXTO_OSCURO);
        Font fBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, COLOR_TEXTO_OSCURO);
        Font fEstadoBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, colorEstadoTexto);
        Font fMini = FontFactory.getFont(FontFactory.HELVETICA, 7, COLOR_TEXTO_OSCURO);
        Font fMiniAccento = FontFactory.getFont(FontFactory.HELVETICA, 7, colorEstadoTexto);

        // 1. LOGO DE LA EMPRESA
        boolean logoCargado = false;
        try {
            if (venta.getEmpresa().getLogoUrl() != null && !venta.getEmpresa().getLogoUrl().isEmpty()) {
                File logoFile = new File(venta.getEmpresa().getLogoUrl());
                if (logoFile.exists()) {
                    Image imagen = Image.getInstance(logoFile.getAbsolutePath());
                    imagen.scaleToFit(140, 55);
                    imagen.setAlignment(Element.ALIGN_CENTER);
                    document.add(imagen);
                    logoCargado = true;
                }
            }
        } catch (Exception ignored) {
        }

        if (!logoCargado) {
            try {
                File logoLocal = new File(RUTA_LOGO_LOCAL);
                if (logoLocal.exists()) {
                    Image imagen = Image.getInstance(logoLocal.getAbsolutePath());
                    imagen.scaleToFit(140, 55);
                    imagen.setAlignment(Element.ALIGN_CENTER);
                    document.add(imagen);
                }
            } catch (Exception ignored) {
            }
        }

        // 2. DATOS DE LA EMPRESA
        Paragraph empresa = new Paragraph(
                venta.getEmpresa().getNombre() + "\n" +
                        "RUC: " + valor(venta.getEmpresa().getRuc()) + "\n" +
                        "Tel: " + valor(venta.getEmpresa().getTelefono()) + "\n" +
                        valor(venta.getEmpresa().getDireccion()) + "\n" +
                        "www.fersal.com.py",
                fMini
        );
        empresa.setAlignment(Element.ALIGN_CENTER);
        empresa.setSpacingAfter(4);
        document.add(empresa);

        document.add(linea(fMini));

        // 3. BANNER PRINCIPAL DEL TIPO DE COMPROBANTE
        String tituloBanner = esDeuda ? "COMPROBANTE DE VENTA A CRÉDITO" : "TICKET DE VENTA";

        PdfPTable bannerTabla = new PdfPTable(1);
        bannerTabla.setWidthPercentage(100);

        PdfPCell celdaBanner = new PdfPCell();
        celdaBanner.setBackgroundColor(colorEstadoBg);
        celdaBanner.setBorder(Rectangle.NO_BORDER);
        celdaBanner.setPadding(5);
        celdaBanner.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph pBanner = new Paragraph(tituloBanner + "\n", fBannerTitulo);
        pBanner.add(new Chunk("N° " + venta.getNumero(), fNumeroAccento));
        pBanner.setAlignment(Element.ALIGN_CENTER);

        celdaBanner.addElement(pBanner);
        bannerTabla.addCell(celdaBanner);
        bannerTabla.setSpacingAfter(6);
        document.add(bannerTabla);

        // 4. DATOS DEL CLIENTE Y COMPRA
        PdfPTable datos = new PdfPTable(2);
        datos.setWidthPercentage(100);
        datos.setWidths(new float[]{3.5f, 6.5f});

        agregarFila(datos, "Fecha:", venta.getFecha() != null ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "-", fNormal, fNormal);
        agregarFila(datos, "Cliente:", venta.getCliente() != null ? venta.getCliente().getNombre() : "Consumidor Final", fNormal, fNormal);

        if (venta.getCliente() != null) {
            agregarFila(datos, "CI/RUC:", valor(venta.getCliente().getDocumento()), fNormal, fNormal);
            agregarFila(datos, "Dirección:", valor(venta.getCliente().getDireccion()), fNormal, fNormal);
        }

        agregarFila(datos, "Forma de pago:", valor(venta.getTipoPago()), fNormal, fBold);

        if (venta.getEstado() != null && !venta.getEstado().isEmpty()) {
            PdfPCell celdaEti = new PdfPCell(new Phrase("Estado:", fNormal));
            celdaEti.setBorder(Rectangle.NO_BORDER);
            datos.addCell(celdaEti);

            PdfPTable subTablaBadge = new PdfPTable(1);
            PdfPCell celdaBadge = new PdfPCell(new Phrase(venta.getEstado().toUpperCase(), fEstadoBold));
            celdaBadge.setBackgroundColor(colorEstadoBg);
            celdaBadge.setBorder(Rectangle.NO_BORDER);
            celdaBadge.setPadding(2);
            celdaBadge.setHorizontalAlignment(Element.ALIGN_LEFT);
            subTablaBadge.addCell(celdaBadge);

            PdfPCell celdaContenedoraBadge = new PdfPCell(subTablaBadge);
            celdaContenedoraBadge.setBorder(Rectangle.NO_BORDER);
            datos.addCell(celdaContenedoraBadge);
        }

        if (venta.getFechaVencimiento() != null) {
            String venceTexto = venta.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (30 días)";
            agregarFila(datos, "Vencimiento:", venceTexto, fNormal, fBold);
        }

        document.add(datos);
        document.add(linea(fMini));

        // 5. TABLA DE PRODUCTOS
        PdfPTable productos = new PdfPTable(4);
        productos.setWidthPercentage(100);
        productos.setWidths(new float[]{3.8f, 1.2f, 2.5f, 2.5f});

        celda(productos, "Producto", fBold, Element.ALIGN_LEFT);
        celda(productos, "Cant.", fBold, Element.ALIGN_CENTER);
        celda(productos, "Precio", fBold, Element.ALIGN_RIGHT);
        celda(productos, "Subtotal", fBold, Element.ALIGN_RIGHT);

        venta.getDetalles().forEach(d -> {
            celda(productos, d.getProducto().getNombre(), fNormal, Element.ALIGN_LEFT);
            celda(productos, String.valueOf(d.getCantidad()), fNormal, Element.ALIGN_CENTER);
            celda(productos, formatearMonto(d.getPrecioUnitario()), fNormal, Element.ALIGN_RIGHT);
            celda(productos, formatearMonto(d.getSubtotal()), fNormal, Element.ALIGN_RIGHT);
        });

        document.add(productos);
        document.add(linea(fMini));

        // 6. TOTAL
        Paragraph total = new Paragraph("TOTAL:   Gs. " + formatearMonto(venta.getTotal()), fTitulo);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(4);
        total.setSpacingAfter(6);
        document.add(total);

        // 7. BLOQUE QR
        try {
            PdfPTable tablaQRBlock = new PdfPTable(2);
            tablaQRBlock.setWidthPercentage(100);
            tablaQRBlock.setWidths(new float[]{3.8f, 6.2f});

            byte[] qr = generarQRCodeBytes(venta.getId().toString(), 85, 80);
            Image qrImg = Image.getInstance(qr);

            PdfPCell celdaQRImage = new PdfPCell(qrImg, true);
            celdaQRImage.setBorder(Rectangle.NO_BORDER);
            celdaQRImage.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaQRBlock.addCell(celdaQRImage);

            PdfPTable subColumnaDerecha = new PdfPTable(1);
            subColumnaDerecha.setWidthPercentage(100);

            PdfPCell celdaIdTexto = new PdfPCell(new Phrase("ID de venta:\n" + venta.getId(), fMini));
            celdaIdTexto.setBorder(Rectangle.NO_BORDER);
            celdaIdTexto.setPaddingBottom(4);
            subColumnaDerecha.addCell(celdaIdTexto);

            PdfPCell celdaBannerExplicativo = new PdfPCell();
            celdaBannerExplicativo.setBackgroundColor(colorEstadoBg);
            celdaBannerExplicativo.setBorder(Rectangle.NO_BORDER);
            celdaBannerExplicativo.setPadding(4);

            Paragraph pExplicativo = new Paragraph("Escaneá el código QR o hacé clic en el enlace para consultar el detalle, estado y vencimiento de esta compra.", fMiniAccento);
            celdaBannerExplicativo.addElement(pExplicativo);
            subColumnaDerecha.addCell(celdaBannerExplicativo);

            PdfPCell celdaDerechaContenedora = new PdfPCell(subColumnaDerecha);
            celdaDerechaContenedora.setBorder(Rectangle.NO_BORDER);
            tablaQRBlock.addCell(celdaDerechaContenedora);

            document.add(tablaQRBlock);
        } catch (Exception ignored) {
        }

        document.add(linea(fMini));

        // 8. PIE DE PÁGINA Y AGRADECIMIENTO
        Paragraph pGracias = new Paragraph("Gracias por tu compra", fBold);
        pGracias.setAlignment(Element.ALIGN_CENTER);
        document.add(pGracias);

        String textoRespaldo = esDeuda
                ? "Este comprobante respalda tu compra a crédito.\nAnte cualquier consulta, comunicate con nosotros."
                : "Comprobante de compra al contado.\nAnte cualquier consulta, comunicate con nosotros.";

        Paragraph pAviso = new Paragraph(textoRespaldo, fMini);
        pAviso.setAlignment(Element.ALIGN_CENTER);
        pAviso.setSpacingAfter(6);
        document.add(pAviso);

        // Banner Final Gris
        PdfPTable tablaPieFinal = new PdfPTable(1);
        tablaPieFinal.setWidthPercentage(100);

        PdfPCell celdaPieFinal = new PdfPCell(new Phrase("¡FERSAL, siempre contigo!", fBold));
        celdaPieFinal.setBackgroundColor(COLOR_GRIS_BG);
        celdaPieFinal.setBorder(Rectangle.NO_BORDER);
        celdaPieFinal.setPadding(4);
        celdaPieFinal.setHorizontalAlignment(Element.ALIGN_CENTER);

        tablaPieFinal.addCell(celdaPieFinal);
        document.add(tablaPieFinal);
    }

    private boolean esCuentaPendiente(Venta venta) {
        String estado = venta.getEstado() != null ? venta.getEstado().toUpperCase() : "";
        String pago = venta.getTipoPago() != null ? venta.getTipoPago().toUpperCase() : "";

        return estado.contains("PENDIENTE") || estado.contains("DEUDA")
                || pago.contains("CREDITO") || pago.contains("CRÉDITO") || pago.contains("FIADO");
    }

    private void agregarFila(PdfPTable tabla, String a, String b, Font fuenteA, Font fuenteB) {
        PdfPCell c1 = new PdfPCell(new Phrase(a, fuenteA));
        c1.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(b, fuenteB));
        c2.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(c2);
    }

    private void celda(PdfPTable tabla, String texto, Font fuente, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setHorizontalAlignment(alineacion);
        tabla.addCell(celda);
    }

    private Paragraph linea(Font fuente) {
        Paragraph p = new Paragraph("--------------------------------------------------", fuente);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(2);
        p.setSpacingAfter(2);
        return p;
    }

    private String valor(String texto) {
        return (texto != null && !texto.trim().isEmpty()) ? texto : "-";
    }

    private String formatearMonto(java.math.BigDecimal monto) {
        if (monto == null) return "0";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "PY"));
        return nf.format(monto);
    }

    private byte[] generarQRCodeBytes(String texto, int width, int height) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(texto, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}