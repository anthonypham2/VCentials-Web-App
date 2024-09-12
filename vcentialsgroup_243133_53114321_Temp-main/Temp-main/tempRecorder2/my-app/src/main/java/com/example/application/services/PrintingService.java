package com.example.application.services;

import com.example.application.data.RecordDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PrintingService {

    private static final Logger logger = LoggerFactory.getLogger(PrintingService.class);

    /**
     * Generates a PDF with the formatted data using Apache PDFBox.
     * @param records The list of records to include in the PDF.
     * @return A byte array containing the generated PDF.
     */
    public byte[] generatePdf(List<RecordDTO> records) {
        try (PDDocument document = new PDDocument()) {
            float y = 750; // Initial y position for text
            PDPage page = createNewPage(document);
            PDPageContentStream contentStream = createContentStream(document, page);

            // Write the table header
            writeHeader(contentStream);

            // Loop through records and write each one to the PDF
            for (RecordDTO record : records) {
                if (y < 50) { // Check if there's enough space for a new line
                    contentStream.close(); // Close the current content stream
                    page = createNewPage(document); // Create a new page
                    contentStream = createContentStream(document, page); // Create a new content stream for the new page
                    y = 750; // Reset y position
                    writeHeader(contentStream); // Write the header on the new page
                }
                // Write the record data to the PDF
                y = writeRecord(contentStream, record, y);
            }
            contentStream.close(); // Close the final content stream

            // Save the document to a byte array output stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray(); // Return the byte array containing the PDF
        } catch (IOException e) {
            logger.error("Error generating PDF", e); // Log any exceptions that occur
        }
        return new byte[0]; // Return an empty byte array if an error occurs
    }

    /**
     * Creates a new page and adds it to the document.
     * @param document The PDF document.
     * @return The newly created page.
     */
    private PDPage createNewPage(PDDocument document) {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        return page;
    }

    /**
     * Creates a new content stream for the given page.
     * @param document The PDF document.
     * @param page The page to create the content stream for.
     * @return The newly created content stream.
     * @throws IOException If an I/O error occurs.
     */
    private PDPageContentStream createContentStream(PDDocument document, PDPage page) throws IOException {
        return new PDPageContentStream(document, page);
    }

    /**
     * Writes the table header to the PDF.
     * @param contentStream The content stream to write to.
     * @throws IOException If an I/O error occurs.
     */
    private void writeHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 750);
        contentStream.showText("Date,Time,Machine Name,Machine Temp F,Room Name,Room Temp F,Location Name,Username");
        contentStream.newLine();
        contentStream.endText();
    }

    /**
     * Writes a single record to the PDF.
     * @param contentStream The content stream to write to.
     * @param record The record to write.
     * @param y The y position to start writing from.
     * @return The new y position after writing the record.
     * @throws IOException If an I/O error occurs.
     */
    private float writeRecord(PDPageContentStream contentStream, RecordDTO record, float y) throws IOException {
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(25, y);
        contentStream.showText(record.getDate() + "," +
                record.getTime() + "," +
                record.getMachine() + "," +
                record.getMachineTempF() + "," +
                record.getRoom() + "," +
                record.getRoomTempF() + "," +
                record.getLocation() + "," +
                record.getUsername());
        contentStream.newLine();
        contentStream.endText();
        return y - 15; // Move y position down for the next record
    }
}
