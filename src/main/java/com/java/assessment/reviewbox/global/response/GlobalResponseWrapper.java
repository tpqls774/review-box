package com.java.assessment.reviewbox.global.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(
            @NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();

        if (ApiResponse.class.isAssignableFrom(parameterType) || ErrorResponse.class.isAssignableFrom(parameterType)) {
            return false;
        }

        String converterName = converterType.getSimpleName();
        return !converterName.contains("ByteArray")
                && !converterName.contains("Resource")
                && !converterName.contains("BufferedImage");
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {
        HttpStatus status = determineHttpStatus(returnType, body);
        response.setStatusCode(status);

        Class<?> parameterType = returnType.getParameterType();

        if (void.class.equals(parameterType) || Void.class.equals(parameterType)) {
            return body;
        }

        if (body == null) {
            return wrapSuccessResponse(status, null);
        }

        if (body instanceof ErrorResponse) {
            return body;
        }

        if (parameterType.equals(String.class)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            ApiResponse<Object> wrappedBody = wrapSuccessResponse(status, body);

            try {
                return objectMapper.writeValueAsString(wrappedBody);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        return wrapSuccessResponse(status, body);
    }

    private HttpStatus determineHttpStatus(MethodParameter returnType, Object body) {
        ResponseStatus responseStatus = returnType.getMethodAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }

        if (body instanceof ErrorResponse e) {
            return HttpStatus.valueOf(e.getStatus());
        }

        return HttpStatus.OK;
    }

    private ApiResponse<Object> wrapSuccessResponse(HttpStatus status, Object body) {
        return ApiResponse.of(status.value(), body, status.getReasonPhrase());
    }
}
