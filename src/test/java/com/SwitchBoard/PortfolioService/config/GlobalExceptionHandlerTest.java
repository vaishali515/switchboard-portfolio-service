package com.SwitchBoard.PortfolioService.config;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void testHandleEntityNotFoundException() {
        // Arrange
        EntityNotFoundException exception = new EntityNotFoundException("Portfolio not found");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleEntityNotFoundException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Portfolio not found", response.getBody().getMessage());
        assertEquals("NOT_FOUND", response.getBody().getErrorCode());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        FieldError fieldError = new FieldError("portfolioRequest", "fullName", "Full name is required");
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleValidationExceptions(validationException, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Validation failed"));
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violation", null);

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleConstraintViolationException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Validation failed"));
        assertEquals("VALIDATION_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void testHandleMultipartException() {
        // Arrange
        MultipartException exception = new MultipartException("Invalid file upload");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleMultipartException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("File upload failed"));
        assertEquals("FILE_UPLOAD_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void testHandleIOException() {
        // Arrange
        IOException exception = new IOException("File read error");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleIOException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("File operation failed"));
        assertEquals("IO_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void testHandleGenericException() {
        // Arrange
        Exception exception = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleGenericException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("An unexpected error occurred"));
        assertEquals("INTERNAL_ERROR", response.getBody().getErrorCode());
    }

    @Test
    void testHandleEntityNotFoundException_WithDifferentMessage() {
        // Arrange
        EntityNotFoundException exception = new EntityNotFoundException("Skill not found with id: 123");

        // Act
        ResponseEntity<ApiResponse> response = globalExceptionHandler.handleEntityNotFoundException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Skill not found"));
    }
}
