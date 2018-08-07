package com.alphawang.user.client;

import com.alphawang.rpc.thrift.user.service.api.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LoginFilter implements Filter {
    @Override 
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                    }
                }
            }
        }

        UserDto userDto = null;
        if (StringUtils.isNotBlank(token)) {
             userDto = requestUserInfo(token);
        }
        
        if (userDto == null) {
            httpServletResponse.sendRedirect("http://localhost:8082/user/login");
        }
    }

    private UserDto requestUserInfo(String token) {
        String url = "http://localhost:8082/user/authentication";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("token", token);

        InputStream in = null;
        try {
            log.info("=== posting user info from {} ", url);
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("failed to request user info. " + response.getStatusLine());
            }

            in = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = in.read(temp)) > 0) {
                 sb.append(new String(temp, 0, len));
            }
            
            UserDto userDto = new ObjectMapper().readValue(sb.toString(), UserDto.class);

            log.info("=== got user info : {} ", userDto);
            return userDto;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
        
    }

    @Override 
    public void destroy() {

    }
}
