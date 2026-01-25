package com.SwitchBoard.PortfolioService.DTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testApiResponse_AllArgsConstructor() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        String message = "Test message";
        String errorCode = "TEST_ERROR";
        String path = "/api/test";
        Object data = "test data";

        // Act
        ApiResponse response = new ApiResponse(true, message, data, errorCode, now, path);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(now, response.getTimestamp());
        assertEquals(path, response.getPath());
    }

    @Test
    void testApiResponse_NoArgsConstructor() {
        // Act
        ApiResponse response = new ApiResponse();

        // Assert
        assertNotNull(response);
    }

    @Test
    void testApiResponse_SuccessWithBooleanHelper() {
        // Act
        ApiResponse response = ApiResponse.success("Operation successful", true);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertNull(response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertNull(response.getPath());
    }

    @Test
    void testApiResponse_SuccessWithDataHelper() {
        // Arrange
        String data = "test data";
        String path = "/api/portfolio";

        // Act
        ApiResponse response = ApiResponse.success("Portfolio created", data, path);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Portfolio created", response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertEquals(path, response.getPath());
    }

    @Test
    void testApiResponse_ErrorHelper() {
        // Arrange
        String message = "Portfolio not found";
        String errorCode = "NOT_FOUND";
        String path = "/api/portfolio/123";

        // Act
        ApiResponse response = ApiResponse.error(message, errorCode, path);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorCode, response.getErrorCode());
        assertNotNull(response.getTimestamp());
        assertEquals(path, response.getPath());
    }

    @Test
    void testApiResponse_SettersAndGetters() {
        // Arrange
        ApiResponse response = new ApiResponse();
        LocalDateTime now = LocalDateTime.now();

        // Act
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData("test data");
        response.setErrorCode("ERROR_CODE");
        response.setTimestamp(now);
        response.setPath("/api/test");

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("test data", response.getData());
        assertEquals("ERROR_CODE", response.getErrorCode());
        assertEquals(now, response.getTimestamp());
        assertEquals("/api/test", response.getPath());
    }

    @Test
    void testApiResponse_SuccessWithNullData() {
        // Act
        ApiResponse response = ApiResponse.success("Success message", null, "/api/test");

        // Assert
        assertTrue(response.isSuccess());
        assertNull(response.getData());
    }

    @Test
    void testApiResponse_ErrorWithNullErrorCode() {
        // Act
        ApiResponse response = ApiResponse.error("Error message", null, "/api/test");

        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getErrorCode());
    }
}
