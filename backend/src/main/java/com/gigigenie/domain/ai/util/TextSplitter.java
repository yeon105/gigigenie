package com.gigigenie.domain.ai.util;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {

    public static List<String> split(String text, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += (chunkSize - overlap)) {
            int end = Math.min(i + chunkSize, text.length());
            chunks.add(text.substring(i, end));
        }
        return chunks;
    }
}