package lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public CommonResponse handleServerException(RuntimeException ex, WebRequest request) {
        return new CommonResponse(500, "UNEXPECTED_ERROR");
    }


    @ExceptionHandler(value = {CustomeException.class})
    public ResponseEntity<CommonResponse> handleCustomException(CustomeException ex, WebRequest request) {

        // Get status code from exception
        int statusCode = ex.getStatus();

        // Create error response
        CommonResponse response = new CommonResponse();
        response.setStatus(1);  // Error
        response.setMessage(ex.getMessage());
        response.setBody(null);

        // Return with correct HTTP status
        return ResponseEntity.status(statusCode).body(response);
        // If statusCode = 409 → 409 Conflict
        // If statusCode = 400 → 400 Bad Request
    }
}
