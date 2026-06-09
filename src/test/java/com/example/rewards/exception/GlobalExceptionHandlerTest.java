package com.example.rewards.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link com.example.rewards.exception.GlobalExceptionHandler}.
 * Exercises how different exceptions are translated into
 * {@link com.example.rewards.exception.ErrorResponse}.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest req;

    /**
     * Initialize a handler and a mocked request used to build error payloads.
     */
    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        req = Mockito.mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/test");
    }

    /**
     * ResourceNotFoundException should produce a 404 payload with the message.
     */
    @Test
    void handleNotFoundReturns404() {
        var ex = new ResourceNotFoundException("missing");
        var resp = handler.handleNotFound(ex, req);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("missing", resp.getBody().getMessage());
    }

    /**
     * Unsupported HTTP methods should return 405.
     */
    @Test
    void handleMethodNotAllowedReturns405() {
        var ex = new HttpRequestMethodNotSupportedException("POST");
        var resp = handler.handleMethodNotAllowed(ex, req);
        assertEquals(405, resp.getStatusCodeValue());
        assertNotNull(resp.getBody().getMessage());
    }

    /**
     * Generic exceptions should be mapped to a 500 payload.
     */
    @Test
    void handleGenericReturns500() {
        var ex = new RuntimeException("boom");
        var resp = handler.handleGeneric(ex, req);
        assertEquals(500, resp.getStatusCodeValue());
        assertEquals("boom", resp.getBody().getMessage());
    }

    /**
     * MethodArgumentNotValidException should aggregate field errors into a message.
     */
    @Test
    void handleValidationAggregatesFieldErrors() throws NoSuchMethodException {
        Method m = this.getClass().getDeclaredMethod("dummy", String.class);
        MethodParameter param = new MethodParameter(m, 0);
        var binding = new BeanPropertyBindingResult(new Object(), "obj");
        binding.addError(new FieldError("obj", "age", "must not be null"));
        var ex = new MethodArgumentNotValidException(param, binding);
        var resp = handler.handleValidation(ex, req);
        assertEquals(400, resp.getStatusCodeValue());
        assertTrue(resp.getBody().getMessage().contains("age must not be null"));
    }

    // dummy method used to create a MethodParameter for the validation test
    @SuppressWarnings("unused")
    private void dummy(String s) {
    }

    /**
     * ConstraintViolationException should include property path info in the
     * message.
     */
    @Test
    void handleConstraintViolationBuildsMessage() {
        ConstraintViolation<Object> cv = new ConstraintViolation<>() {
            @Override
            public String getMessage() {
                return "must be > 0";
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Object getRootBean() {
                return null;
            }

            @Override
            public Class<Object> getRootBeanClass() {
                return Object.class;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public jakarta.validation.Path getPropertyPath() {
                return new jakarta.validation.Path() {
                    @Override
                    public java.util.Iterator<jakarta.validation.Path.Node> iterator() {
                        return java.util.Collections.emptyIterator();
                    }

                    @Override
                    public String toString() {
                        return "field";
                    }
                };
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public jakarta.validation.metadata.ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };

        var ex = new ConstraintViolationException(Collections.singleton(cv));
        var resp = handler.handleConstraintViolation(ex, req);
        assertEquals(400, resp.getStatusCodeValue());
        assertTrue(resp.getBody().getMessage().contains("field"));
    }

    /**
     * Type mismatch errors should be presented as a 400 with the invalid name.
     */
    @Test
    void handleTypeMismatchReturnsBadRequest() throws NoSuchMethodException {
        Method m = this.getClass().getDeclaredMethod("dummy", String.class);
        MethodParameter param = new MethodParameter(m, 0);
        var ex = new MethodArgumentTypeMismatchException("abc", Integer.class, "id", param,
                new IllegalArgumentException());
        var resp = handler.handleTypeMismatch(ex, req);
        assertEquals(400, resp.getStatusCodeValue());
        assertTrue(resp.getBody().getMessage().contains("id"));
    }

    /**
     * NumberFormatException should return a 400 with the exception message.
     */
    @Test
    void handleNumberFormatReturnsBadRequest() {
        var ex = new NumberFormatException("For input string: \"abc\"");
        var resp = handler.handleNumberFormat(ex, req);
        assertEquals(400, resp.getStatusCodeValue());
        assertTrue(resp.getBody().getMessage().contains("input string"));
    }
}
