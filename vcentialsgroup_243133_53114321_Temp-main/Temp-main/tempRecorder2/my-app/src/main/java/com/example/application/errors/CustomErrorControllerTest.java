package com.example.application.errors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomErrorController.
 * These tests verify that the correct error view is returned for various HTTP status codes.
 */
@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    // Mocking the HttpServletRequest to simulate web requests.
    @Mock
    private HttpServletRequest request;

    // Injecting the CustomErrorController to test its methods.
    @InjectMocks
    private CustomErrorController customErrorController;

    /**
     * Setup method to initialize the mocked HttpServletRequest.
     * This method is executed before each test to ensure a clean state.
     */
    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
    }

    /**
     * Test case for handling 400 Bad Request error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_400() {
        // Simulate a 400 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(400);
        // Verify that the controller forwards to the /error-400 view.
        assertEquals("forward:/error-400", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 401 Unauthorized error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_401() {
        // Simulate a 401 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(401);
        // Verify that the controller forwards to the /error-401 view.
        assertEquals("forward:/error-401", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 403 Forbidden error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_403() {
        // Simulate a 403 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(403);
        // Verify that the controller forwards to the /error-403 view.
        assertEquals("forward:/error-403", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 404 Not Found error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_404() {
        // Simulate a 404 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);
        // Verify that the controller forwards to the /error-404 view.
        assertEquals("forward:/error-404", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 408 Request Timeout error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_408() {
        // Simulate a 408 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(408);
        // Verify that the controller forwards to the /error-408 view.
        assertEquals("forward:/error-408", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 500 Internal Server Error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_500() {
        // Simulate a 500 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);
        // Verify that the controller forwards to the /error-500 view.
        assertEquals("forward:/error-500", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 502 Bad Gateway error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_502() {
        // Simulate a 502 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(502);
        // Verify that the controller forwards to the /error-502 view.
        assertEquals("forward:/error-502", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 503 Service Unavailable error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_503() {
        // Simulate a 503 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(503);
        // Verify that the controller forwards to the /error-503 view.
        assertEquals("forward:/error-503", customErrorController.handleError(request));
    }

    /**
     * Test case for handling 504 Gateway Timeout error.
     * It verifies that the controller forwards to the correct error view.
     */
    @Test
    public void testHandleError_504() {
        // Simulate a 504 error by setting the ERROR_STATUS_CODE attribute in the request.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(504);
        // Verify that the controller forwards to the /error-504 view.
        assertEquals("forward:/error-504", customErrorController.handleError(request));
    }

    /**
     * Test case for handling an unspecified error code.
     * It verifies that the controller forwards to a generic error view.
     */
    @Test
    public void testHandleError_Default() {
        // Simulate an unspecified error by setting the ERROR_STATUS_CODE attribute to an unknown value.
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(999);
        // Verify that the controller forwards to the /error-generic view.
        assertEquals("forward:/error-generic", customErrorController.handleError(request));
    }
}
