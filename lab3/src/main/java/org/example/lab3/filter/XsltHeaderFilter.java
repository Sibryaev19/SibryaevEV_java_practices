package org.example.lab3.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class XsltHeaderFilter implements Filter {

    private static final String STYLESHEET_PI = "<?xml-stylesheet type=\"text/xsl\" href=\"/transform.xsl\"?>";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, cachingResponse);

        String contentType = cachingResponse.getContentType();
        byte[] body = cachingResponse.getContentAsByteArray();

        if (contentType != null && contentType.contains(MediaType.APPLICATION_XML_VALUE) && body.length > 0) {
            String original = new String(body, StandardCharsets.UTF_8);
            String withPi;

            if (original.contains("<?xml-stylesheet")) {
                withPi = original;
            } else if (original.startsWith("<?xml")) {
                int endDecl = original.indexOf("?>");
                if (endDecl > -1) {
                    withPi = original.substring(0, endDecl + 2) + "\n" + STYLESHEET_PI + original.substring(endDecl + 2);
                } else {
                    withPi = STYLESHEET_PI + "\n" + original;
                }
            } else {
                withPi = STYLESHEET_PI + "\n" + original;
            }

            byte[] updatedBody = withPi.getBytes(StandardCharsets.UTF_8);
            cachingResponse.resetBuffer();
            cachingResponse.setContentLength(updatedBody.length);
            cachingResponse.getOutputStream().write(updatedBody);
        }

        // Отдаём финальный ответ
        cachingResponse.copyBodyToResponse();
    }
}