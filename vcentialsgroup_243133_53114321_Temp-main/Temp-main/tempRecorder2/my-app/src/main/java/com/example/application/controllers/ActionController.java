package com.example.application.controllers;

import com.example.application.data.RecordDTO;
import com.example.application.services.PrintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActionController {

    @Autowired
    private PrintingService printingService;

    /**
     * Endpoint to generate a PDF. It invokes the printing service to format and generate a PDF file.
     * @param records The list of records to include in the PDF.
     * @param response The HTTP response to write the PDF data to.
     * @throws IOException If an input or output exception occurs.
     */
    @PostMapping("/generate-pdf")
    public void generatePdf(@RequestBody List<RecordDTO> records, HttpServletResponse response) throws IOException {
        byte[] pdfData = printingService.generatePdf(records);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=data.pdf");
        response.setContentLength(pdfData.length);

        response.getOutputStream().write(pdfData);
        response.getOutputStream().flush();
    }
}
