package com.example.educationplatform.component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CatalogPaginationData {
    private String[] selectedLevels;
    private String tutorName = "";
    private int page = 0;
    private int pageSize = 6;
    private int totalPages = 1;
}
