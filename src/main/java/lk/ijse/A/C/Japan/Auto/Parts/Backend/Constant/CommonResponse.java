package lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private int status;
    private Object body;
    private String message;

    public CommonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommonResponse(String message) {
        this.status = 1;
        this.body = null;
        this.message = message;
    }
}
