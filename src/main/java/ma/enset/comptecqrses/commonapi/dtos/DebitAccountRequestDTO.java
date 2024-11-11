package ma.enset.comptecqrses.commonapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DebitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
