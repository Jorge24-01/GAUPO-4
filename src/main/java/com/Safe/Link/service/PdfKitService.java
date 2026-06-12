package com.Safe.Link.service;

import com.Safe.Link.entities.ProgresoChecklist;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.ProgresoChecklistRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfKitService {

    private final ProgresoChecklistRepository checklistRepository;
    private final UsuarioRepository usuarioRepository;

    // US-30 — Escenarios 1 y 3: genera el PDF con ítems default + personalizados
    public byte[] generarPdfKit(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        List<ProgresoChecklist> items = checklistRepository.findByUsuarioId(usuarioId);

        // Si el usuario no tiene ítems guardados, usamos la lista default de INDECI
        if (items.isEmpty()) {
            items = buildItemsDefault(usuarioId);
        }

        return buildPdf(usuario, items);
    }

    private byte[] buildPdf(Usuario usuario, List<ProgresoChecklist> items) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 40, 40, 60, 40);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // -- Fuentes --
            Font fuenteTitulo   = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(30, 80, 140));
            Font fuenteSubtitle = new Font(Font.HELVETICA, 11, Font.NORMAL, new Color(90, 90, 90));
            Font fuenteHeader   = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
            Font fuenteItem     = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
            Font fuenteItemOk   = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(30, 130, 30));
            Font fuentePie      = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(150, 150, 150));

            // -- Encabezado --
            Paragraph titulo = new Paragraph("SafeLink — Kit de Emergencia", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(4);
            doc.add(titulo);

            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph subtitulo = new Paragraph(
                    "Preparado para: " + usuario.getNombre() + " " + usuario.getApellido()
                            + "   |   Fecha: " + fechaHoy, fuenteSubtitle);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(16);
            doc.add(subtitulo);

            // -- Línea separadora --
            doc.add(new Chunk(new com.lowagie.text.pdf.draw.LineSeparator()));
            doc.add(Chunk.NEWLINE);

            // -- Agrupar ítems por categoría --
            Map<String, List<ProgresoChecklist>> porCategoria = items.stream()
                    .collect(Collectors.groupingBy(
                            i -> i.getCategoria() != null ? i.getCategoria() : "General"
                    ));

            for (Map.Entry<String, List<ProgresoChecklist>> entrada : porCategoria.entrySet()) {
                // Título de categoría
                Paragraph catTitulo = new Paragraph(entrada.getKey().toUpperCase(), fuenteTitulo);
                catTitulo.setFont(new Font(Font.HELVETICA, 12, Font.BOLD, new Color(30, 80, 140)));
                catTitulo.setSpacingBefore(12);
                catTitulo.setSpacingAfter(6);
                doc.add(catTitulo);

                // Tabla de ítems
                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{0.8f, 5f, 1.5f});

                // Cabecera de tabla
                agregarCeldaHeader(tabla, "#",          fuenteHeader);
                agregarCeldaHeader(tabla, "Ítem",       fuenteHeader);
                agregarCeldaHeader(tabla, "Estado",     fuenteHeader);

                // Filas
                List<ProgresoChecklist> listaCategoria = entrada.getValue();
                for (int i = 0; i < listaCategoria.size(); i++) {
                    ProgresoChecklist item = listaCategoria.get(i);
                    boolean completado = Boolean.TRUE.equals(item.getCompletado());

                    Color bgFila = (i % 2 == 0) ? new Color(245, 248, 255) : Color.WHITE;

                    agregarCeldaFila(tabla, String.valueOf(i + 1), fuenteItem, bgFila, Element.ALIGN_CENTER);
                    agregarCeldaFila(tabla, item.getNombreItem()
                                    + (Boolean.TRUE.equals(item.getEsPersonalizado()) ? " *" : ""),
                            fuenteItem, bgFila, Element.ALIGN_LEFT);
                    agregarCeldaFila(tabla, completado ? "✓ Listo" : "Pendiente",
                            completado ? fuenteItemOk : fuenteItem, bgFila, Element.ALIGN_CENTER);
                }

                doc.add(tabla);
            }

            // -- Pie de página --
            doc.add(Chunk.NEWLINE);
            doc.add(new Chunk(new com.lowagie.text.pdf.draw.LineSeparator()));
            Paragraph pie = new Paragraph(
                    "* Ítem personalizado por el usuario. "
                            + "Lista base basada en recomendaciones de INDECI (indeci.gob.pe).",
                    fuentePie);
            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(6);
            doc.add(pie);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF del kit: " + e.getMessage(), e);
        }
    }

    // -- Helpers de tabla --
    private void agregarCeldaHeader(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(new Color(30, 80, 140));
        celda.setPadding(6);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBorderColor(new Color(30, 80, 140));
        tabla.addCell(celda);
    }

    private void agregarCeldaFila(PdfPTable tabla, String texto, Font fuente,
                                  Color bgColor, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(bgColor);
        celda.setPadding(5);
        celda.setHorizontalAlignment(alineacion);
        celda.setBorderColor(new Color(200, 210, 230));
        tabla.addCell(celda);
    }

    // US-30 — Escenario 3: lista default INDECI cuando el usuario no tiene ítems guardados
    private List<ProgresoChecklist> buildItemsDefault(Long usuarioId) {
        return List.of(
                buildItem(usuarioId, "Agua (mínimo 4 litros por persona)",    "Agua y Alimentos", false),
                buildItem(usuarioId, "Alimentos no perecibles (3 días)",       "Agua y Alimentos", false),
                buildItem(usuarioId, "Linterna con pilas de repuesto",         "Equipo Básico",    false),
                buildItem(usuarioId, "Radio a pilas o manivela",               "Equipo Básico",    false),
                buildItem(usuarioId, "Botiquín de primeros auxilios",          "Salud",            false),
                buildItem(usuarioId, "Medicamentos de uso regular",            "Salud",            false),
                buildItem(usuarioId, "Copia de documentos importantes (DNI)",  "Documentos",       false),
                buildItem(usuarioId, "Dinero en efectivo",                     "Documentos",       false),
                buildItem(usuarioId, "Ropa abrigadora y calzado cerrado",      "Ropa",             false),
                buildItem(usuarioId, "Manta o bolsa de dormir",                "Ropa",             false),
                buildItem(usuarioId, "Silbato para señales de emergencia",     "Equipo Básico",    false),
                buildItem(usuarioId, "Mascarillas y guantes",                  "Salud",            false)
        );
    }

    private ProgresoChecklist buildItem(Long usuarioId, String nombre,
                                        String categoria, boolean completado) {
        return ProgresoChecklist.builder()
                .usuarioId(usuarioId)
                .nombreItem(nombre)
                .categoria(categoria)
                .esPersonalizado(false)
                .completado(completado)
                .build();
    }
}