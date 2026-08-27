package lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomeException extends RuntimeException {
    private int status;
    private String message;

    public CustomeException(String message) {
        super(message);
        this.status = 400;
        this.message = message;
    }
}