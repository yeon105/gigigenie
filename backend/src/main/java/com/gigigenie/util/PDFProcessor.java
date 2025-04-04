package com.gigigenie.util;

import com.gigigenie.dto.DocumentDataDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PDFProcessor {

    public List<DocumentDataDTO> extractTextFromPdf(MultipartFile file, int chunkSize, int chunkOverlap) throws IOException {
        List<DocumentDataDTO> documents = new ArrayList<>();

        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();

        List<String> chunks = chunkText(text, chunkSize, chunkOverlap);
        for (String chunk : chunks) {
            DocumentDataDTO docData = new DocumentDataDTO();
            docData.setDocument(chunk);
            docData.setMetadata("{}");
            documents.add(docData);
        }

        return documents;
    }

    private List<String> chunkText(String text, int chunkSize, int chunkOverlap) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
            start += (chunkSize - chunkOverlap);
        }
        return chunks;
    }
}

