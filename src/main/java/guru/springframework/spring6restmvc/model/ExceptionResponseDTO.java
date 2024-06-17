package guru.springframework.spring6restmvc.model;

import lombok.*;

@Getter
@Setter
public class ExceptionResponseDTO {
    private int id;
    private String message;

    public ExceptionResponseDTO(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
