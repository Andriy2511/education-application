package com.example.educationplatform.component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ListPaginationData {
    int pageSize = 10;
    int page = 0;
    Long totalRecords = 10L;
    String lastVisitedPage = "";
}
