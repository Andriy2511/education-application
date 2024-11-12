package com.example.educationplatform.filter;

import com.example.educationplatform.component.ListPaginationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class ListPaginationFilter extends OncePerRequestFilter {

    private final ListPaginationData listPaginationData;
    private final List<String> allowedUrlList = new ArrayList<>();

    @Autowired
    public ListPaginationFilter(ListPaginationData listPaginationData) {
        this.listPaginationData = listPaginationData;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        changePageAndPageSizeForList(request);
        filterChain.doFilter(request, response);
    }

    private void changePageAndPageSizeForList(HttpServletRequest request){
        String requestPath = request.getRequestURI();
        List<String> allowedUrls = new ArrayList<>(getAllowedUrlList());
        Iterator<String> iterator = allowedUrls.iterator();
        try {
            while (iterator.hasNext()) {
                String allowedUrl = iterator.next();
                if (requestPath.startsWith(allowedUrl)) {
                    String paramPage = request.getParameter("page");
                    String paramPageSize = request.getParameter("pageSize");
                    setFirstPageIfUserChangePage(requestPath);
                    if (paramPage != null) {
                        try {
                            Long totalRecords = listPaginationData.getTotalRecords();
                            int page = Integer.parseInt(paramPage) + listPaginationData.getPage();
                            if(page >= 0 && (long) page * listPaginationData.getPageSize() < totalRecords)
                                listPaginationData.setPage(page);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    if (paramPageSize != null) {
                        try {
                            if(Integer.parseInt(paramPageSize) > 0)
                                listPaginationData.setPageSize(Integer.parseInt(paramPageSize));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    setLastPageIfPageBiggerThanTotalPage();
                    break;
                }
            }
        } catch (ConcurrentModificationException exception){
            log.error(exception.getMessage());
        }
    }

    private List<String> getAllowedUrlList(){
        allowedUrlList.add("/tutor/showTutorTimeSlot");

        allowedUrlList.add("/student/showStudentLessons");

        allowedUrlList.add("/admin/showStudentsList");
        allowedUrlList.add("/admin/showTutorsList");
        allowedUrlList.add("/admin/showCompletedTests");
        allowedUrlList.add("/admin/showUncompletedTests");

        allowedUrlList.add("/catalog/showTutorSchedule");

        return allowedUrlList;
    }

    private void setFirstPageIfUserChangePage(String requestPath){
        if (!requestPath.equals(listPaginationData.getLastVisitedPage())) {
            listPaginationData.setPage(0);
            listPaginationData.setLastVisitedPage(requestPath);
        }
    }

    private void setLastPageIfPageBiggerThanTotalPage(){
        Long totalRecords = listPaginationData.getTotalRecords();
        int numberOfRecords = listPaginationData.getPage() * listPaginationData.getPageSize();
        while (numberOfRecords > totalRecords && listPaginationData.getPage() > 0) {
            listPaginationData.setPage(listPaginationData.getPage() - 1);
            numberOfRecords = listPaginationData.getPage() * listPaginationData.getPageSize();
        }
    }
}

