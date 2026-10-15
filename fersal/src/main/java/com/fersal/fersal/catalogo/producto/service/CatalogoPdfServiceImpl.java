package com.fersal.fersal.catalogo.producto.service;

import com.fersal.fersal.catalogo.producto.dto.CatalogoPdfResponse;
import com.fersal.fersal.catalogo.producto.entity.Producto;
import com.fersal.fersal.empresa.entity.Empresa;
import com.fersal.fersal.empresa.repository.EmpresaRepository;
import com.fersal.fersal.inventario.stock.entity.Stock;
import com.fersal.fersal.inventario.stock.repository.StockRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogoPdfServiceImpl implements CatalogoPdfService {

    private static final String RUTA_LOGO_FALLBACK = "C:\\Users\\Alberto Ferreira\\Desktop\\treinta\\fersal\\uploads\\Logo\\fersal_logo_letras.png";

    // Paleta Corporativa FERSAL
    private static final Color COLOR_PRIMARY = new Color(27, 98, 212);        // #1B62D4
    private static final Color COLOR_PRIMARY_BG = new Color(224, 237, 255);    // #E0EDFF
    private static final Color COLOR_VERDE_TEXTO = new Color(21, 128, 61);     // #15803D
    private static final Color COLOR_VERDE_BG = new Color(220, 252, 231);      // #DCFCE7
    private static final Color COLOR_TEXTO_OSCURO = new Color(15, 23, 42);     // #0F172A
    private static final Color COLOR_TEXTO_MUTED = new Color(100, 116, 139);   // #64748B
    private static final Color COLOR_CARD_BG = new Color(248, 250, 252);       // #F8FAFC
    private static final Color COLOR_BORDER = new Color(226, 232, 240);        // #E2E8F0

    private final StockRepository stockRepository;
    private final EmpresaRepository empresaRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public CatalogoPdfResponse generarCatalogoPdf(UUID empresaId, UUID sucursalId) {
        Empresa empresa = empresaRepository.findById(empresaId).orElseThrow();
        List<Stock> stocks = stockRepository.findByEmpresaIdAndSucursalId(empresaId, sucursalId);

        byte[] pdfBytes = construirPdfCatalogo(empresa, stocks);

        String fechaStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        return CatalogoPdfResponse.builder()
                .nombreArchivo("catalogo-fersal-" + fechaStr + ".pdf")
                .contenidoBase64(Base64.getEncoder().encodeToString(pdfBytes))
                .build();
    }

    private byte[] construirPdfCatalogo(Empresa empresa, List<Stock> stocks) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 28, 28, 30, 30);
            PdfWriter.getInstance(document, output);
            document.open();

            // Tipografías
            Font fEmpresa = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, COLOR_TEXTO_OSCURO);
            Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_TEXTO_MUTED);
            Font fBannerTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, COLOR_PRIMARY);
            Font fBannerSub = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_TEXTO_MUTED);
            Font fProdTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO_OSCURO);
            Font fProdMeta = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_TEXTO_MUTED);
            Font fProdPrecio = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_PRIMARY);
            Font fBadgeVerde = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, COLOR_VERDE_TEXTO);
            Font fFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_TEXTO_MUTED);
            Font fFooterBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, COLOR_TEXTO_OSCURO);

            // 1. HEADER
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{40f, 60f});

            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Image logoImg = obtenerImagenLogo(empresa);
            if (logoImg != null) {
                logoImg.scaleToFit(130, 48);
                celdaLogo.addElement(logoImg);
            } else {
                Paragraph pFallback = new Paragraph(empresa.getNombre().toUpperCase(), fEmpresa);
                celdaLogo.addElement(pFallback);
            }
            headerTable.addCell(celdaLogo);

            PdfPCell celdaDatosEmpresa = new PdfPCell();
            celdaDatosEmpresa.setBorder(Rectangle.NO_BORDER);
            celdaDatosEmpresa.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaDatosEmpresa.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph pEmpresa = new Paragraph();
            pEmpresa.setAlignment(Element.ALIGN_RIGHT);
            pEmpresa.add(new Chunk(empresa.getNombre().toUpperCase() + "\n", fEmpresa));
            pEmpresa.add(new Chunk("RUC: " + valor(empresa.getRuc()) + "  •  Tel: " + valor(empresa.getTelefono()) + "\n", fSub));
            pEmpresa.add(new Chunk(valor(empresa.getDireccion()) + "\n", fSub));
            pEmpresa.add(new Chunk("Actualizado al: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fSub));
            celdaDatosEmpresa.addElement(pEmpresa);
            headerTable.addCell(celdaDatosEmpresa);

            document.add(headerTable);

            // Separador
            PdfPTable sep = new PdfPTable(1);
            sep.setWidthPercentage(100);
            sep.setSpacingBefore(6);
            sep.setSpacingAfter(10);
            PdfPCell cSep = new PdfPCell();
            cSep.setBackgroundColor(COLOR_PRIMARY);
            cSep.setFixedHeight(2f);
            cSep.setBorder(Rectangle.NO_BORDER);
            sep.addCell(cSep);
            document.add(sep);

            // 2. BANNER
            PdfPTable banner = new PdfPTable(1);
            banner.setWidthPercentage(100);
            PdfPCell cBanner = new PdfPCell();
            cBanner.setBackgroundColor(COLOR_PRIMARY_BG);
            cBanner.setBorder(Rectangle.NO_BORDER);
            cBanner.setPadding(8);

            Paragraph pBanner = new Paragraph("CATÁLOGO OFICIAL DE PRODUCTOS Y PRECIOS\n", fBannerTitulo);
            pBanner.add(new Chunk("Lista de precios vigentes en Guaraníes (Gs.) para clientes mayoristas y minoristas.", fBannerSub));
            pBanner.setAlignment(Element.ALIGN_CENTER);
            cBanner.addElement(pBanner);
            banner.addCell(cBanner);
            banner.setSpacingAfter(12);
            document.add(banner);

            // 3. GRILLA DE PRODUCTOS
            PdfPTable grid = new PdfPTable(2);
            grid.setWidthPercentage(100);
            grid.setWidths(new float[]{50f, 50f});

            for (Stock s : stocks) {
                Producto p = s.getProducto();
                if (!Boolean.TRUE.equals(p.getActivo())) continue;

                PdfPCell cardCell = new PdfPCell();
                cardCell.setBorder(Rectangle.NO_BORDER);
                cardCell.setPadding(4);

                PdfPTable itemTable = new PdfPTable(2);
                itemTable.setWidthPercentage(100);
                itemTable.setWidths(new float[]{32f, 68f});

                // Foto del producto
                PdfPCell fotoCell = new PdfPCell();
                fotoCell.setBackgroundColor(Color.WHITE);
                fotoCell.setBorderColor(COLOR_BORDER);
                fotoCell.setPadding(4);
                fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                fotoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                Image imgProd = resolverImagenProducto(p.getImagenUrl());
                if (imgProd != null) {
                    imgProd.scaleToFit(58, 58);
                    fotoCell.addElement(imgProd);
                } else {
                    Paragraph placeholder = new Paragraph("📦\nFERSAL", fProdMeta);
                    placeholder.setAlignment(Element.ALIGN_CENTER);
                    fotoCell.addElement(placeholder);
                }
                itemTable.addCell(fotoCell);

                // Info del producto
                PdfPCell infoCell = new PdfPCell();
                infoCell.setBackgroundColor(COLOR_CARD_BG);
                infoCell.setBorderColor(COLOR_BORDER);
                infoCell.setPadding(6);
                infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                Paragraph pTit = new Paragraph(p.getNombre(), fProdTitulo);
                pTit.setLeading(11f);
                infoCell.addElement(pTit);

                String meta = "Cód: " + valor(p.getCodigo());
                if (p.getCategoria() != null && p.getCategoria().getNombre() != null) {
                    meta += " | " + p.getCategoria().getNombre();
                }
                Paragraph pMeta = new Paragraph(meta, fProdMeta);
                pMeta.setSpacingBefore(2);
                infoCell.addElement(pMeta);

                PdfPTable badgeTable = new PdfPTable(1);
                badgeTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                badgeTable.setTotalWidth(68f);
                badgeTable.setLockedWidth(true);
                PdfPCell bCell = new PdfPCell(new Phrase("✓ DISPONIBLE", fBadgeVerde));
                bCell.setBackgroundColor(COLOR_VERDE_BG);
                bCell.setBorder(Rectangle.NO_BORDER);
                bCell.setPadding(2);
                bCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                badgeTable.addCell(bCell);
                badgeTable.setSpacingBefore(3);
                infoCell.addElement(badgeTable);

                Paragraph pPrecio = new Paragraph("Gs. " + formatearMonto(p.getPrecioVenta()), fProdPrecio);
                pPrecio.setSpacingBefore(4);
                infoCell.addElement(pPrecio);

                itemTable.addCell(infoCell);
                cardCell.addElement(itemTable);
                grid.addCell(cardCell);
            }

            if (grid.size() % 2 != 0) {
                PdfPCell celdaVacia = new PdfPCell();
                celdaVacia.setBorder(Rectangle.NO_BORDER);
                grid.addCell(celdaVacia);
            }

            document.add(grid);

            // 4. FOOTER CON QR
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            footerTable.setWidths(new float[]{18f, 82f});
            footerTable.setSpacingBefore(16);

            String qrContenido = "EMPRESA: " + empresa.getNombre() + "\nRUC: " + valor(empresa.getRuc()) + "\nTEL: " + valor(empresa.getTelefono());
            byte[] qrBytes = generarQRCodeBytes(qrContenido);
            Image qrImg = Image.getInstance(qrBytes);
            PdfPCell qrCell = new PdfPCell(qrImg, true);
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            footerTable.addCell(qrCell);

            PdfPCell textoFooterCell = new PdfPCell();
            textoFooterCell.setBorder(Rectangle.NO_BORDER);
            textoFooterCell.setPaddingLeft(10);
            textoFooterCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph pF1 = new Paragraph("¡Hacé tus pedidos o consultas directamente con nosotros!", fFooterBold);
            Paragraph pF2 = new Paragraph("Escanéa el código QR para agendar nuestros datos de contacto. Disponibilidad sujeta a confirmación en el punto de venta.\nFERSAL Software de Gestión Empresarial • www.fersal.com.py", fFooter);
            pF2.setSpacingBefore(3);
            textoFooterCell.addElement(pF1);
            textoFooterCell.addElement(pF2);
            footerTable.addCell(textoFooterCell);

            document.add(footerTable);

            document.close();
            return output.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar PDF del catálogo: ", e);
            throw new RuntimeException("Error al generar el PDF del catálogo", e);
        }
    }

    private Image obtenerImagenLogo(Empresa empresa) {
        try {
            if (empresa.getLogoUrl() != null && !empresa.getLogoUrl().trim().isEmpty()) {
                Image img = resolverImagenProducto(empresa.getLogoUrl());
                if (img != null) return img;
            }
            File fallback = new File(RUTA_LOGO_FALLBACK);
            if (fallback.exists()) {
                return Image.getInstance(fallback.getAbsolutePath());
            }
        } catch (Exception e) {
            log.warn("No se pudo cargar el logo de la empresa: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Resuelve robustamente imágenes locales, absolutas, relativas al proyecto o URLs web.
     */
    private Image resolverImagenProducto(String rutaOriginal) {
        if (rutaOriginal == null || rutaOriginal.trim().isEmpty()) {
            return null;
        }

        String ruta = rutaOriginal.trim().replace('\\', '/');

        try {
            // 1. Si es URL Web (http/https)
            if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
                try (InputStream in = URI.create(ruta).toURL().openStream()) {
                    byte[] bytes = in.readAllBytes();
                    return Image.getInstance(bytes);
                }
            }

            // Normalización: quitar barra inicial si existe para permitir resolución relativa
            String rutaSinBarra = ruta.startsWith("/") ? ruta.substring(1) : ruta;

            // 2. Ruta exacta absoluta o tal cual viene
            Path pathDirecto = Paths.get(rutaOriginal);
            if (Files.exists(pathDirecto) && Files.isRegularFile(pathDirecto)) {
                return Image.getInstance(pathDirecto.toAbsolutePath().toString());
            }

            // 3. Resolución con uploadDir configurado (ej: uploads/ o C:\...uploads)
            Path pathUpload = Paths.get(uploadDir).resolve(rutaSinBarra);
            if (Files.exists(pathUpload) && Files.isRegularFile(pathUpload)) {
                return Image.getInstance(pathUpload.toAbsolutePath().toString());
            }

            // 4. Si la ruta guardada ya contenía "uploads/" y uploadDir también es "uploads"
            if (rutaSinBarra.startsWith("uploads/")) {
                String subRuta = rutaSinBarra.substring("uploads/".length());
                Path pathLimpio = Paths.get(uploadDir).resolve(subRuta);
                if (Files.exists(pathLimpio) && Files.isRegularFile(pathLimpio)) {
                    return Image.getInstance(pathLimpio.toAbsolutePath().toString());
                }
            }

            // 5. Búsqueda relativa a la carpeta de ejecución de la app
            Path pathRelativoRaiz = Paths.get(".").resolve(rutaSinBarra);
            if (Files.exists(pathRelativoRaiz) && Files.isRegularFile(pathRelativoRaiz)) {
                return Image.getInstance(pathRelativoRaiz.toAbsolutePath().toString());
            }

            log.warn("Archivo de imagen no encontrado en ninguna ruta: '{}'", rutaOriginal);
        } catch (Exception e) {
            log.warn("Error al procesar la imagen '{}': {}", rutaOriginal, e.getMessage());
        }

        return null;
    }

    private String valor(String texto) {
        return (texto != null && !texto.trim().isEmpty()) ? texto : "-";
    }

    private String formatearMonto(BigDecimal monto) {
        if (monto == null) return "0";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "PY"));
        return nf.format(monto);
    }

    private byte[] generarQRCodeBytes(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 70, 70);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}