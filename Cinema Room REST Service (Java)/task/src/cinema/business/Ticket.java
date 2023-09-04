package cinema.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends ReturnedTicket {
    private String token;

    @JsonProperty("ticket")
    private Seat seat;

//    public Ticket(Seat seat) {
//        this.token = UUID.randomUUID();
//        this.seat = seat;
//    }
}
