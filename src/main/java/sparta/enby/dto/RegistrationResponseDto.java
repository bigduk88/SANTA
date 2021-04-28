package sparta.enby.dto;

import lombok.Data;

@Data
public class RegistrationResponseDto {
    private boolean register;
    private String comment;

    public RegistrationResponseDto(boolean register, String comment){
        this.register = register;
        this.comment = comment;
    }
}
