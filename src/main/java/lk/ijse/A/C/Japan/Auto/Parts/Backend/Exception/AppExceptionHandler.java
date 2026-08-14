package lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class AppExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public CommonResponse handleServerException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return new CommonResponse(500, "UNEXPECTED_ERROR");
    }

    @ExceptionHandler(value = {CustomeException.class})
    public ResponseEntity<CommonResponse> handleCustomException(CustomeException ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(new CommonResponse(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
