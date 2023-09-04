package cinema.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Seat extends SeatNumber {

    private int price;
    @JsonIgnore
    private boolean available;
    @JsonIgnore
    private String token;

    public Seat (int row, int column) {
        this.row = row;
        this.column = column;
        this.price = row <= 4 ? 10 : 8;
        this.available = true;
        this.token = "";
    }


}
