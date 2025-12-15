package org.example.lab3.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
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

        System.out.println("XsltHeaderFilter: Content-Type = " + contentType);
        System.out.println("XsltHeaderFilter: Body length = " + body.length);

        // ИСПРАВЛЕНО: Добавляем xhtml+xml в проверку
        if (contentType != null &&
                (contentType.contains("application/xml") ||
                        contentType.contains("text/xml") ||
                        contentType.contains("application/xhtml+xml")) &&
                body.length > 0) {

            System.out.println("XsltHeaderFilter: Adding XSLT stylesheet to XML/XHTML");

            String original = new String(body, StandardCharsets.UTF_8);
            String withPi;

            original = original.trim();

            if (original.contains("<?xml-stylesheet")) {
                withPi = original;
            } else if (original.startsWith("<?xml")) {
                int endDecl = original.indexOf("?>");
                if (endDecl > -1) {
                    withPi = original.substring(0, endDecl + 2) + "\n" +
                            STYLESHEET_PI +
                            original.substring(endDecl + 2);
                } else {
                    withPi = original + "\n" + STYLESHEET_PI;
                }
            } else {
                withPi = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        STYLESHEET_PI + "\n" +
                        original;
            }

            byte[] updatedBody = withPi.getBytes(StandardCharsets.UTF_8);
            cachingResponse.resetBuffer();
            cachingResponse.setContentLength(updatedBody.length);
            cachingResponse.getOutputStream().write(updatedBody);

            System.out.println("XsltHeaderFilter: XSLT added successfully");
        } else {
            System.out.println("XsltHeaderFilter: Not XML, skipping");
        }

        cachingResponse.copyBodyToResponse();
    }
}