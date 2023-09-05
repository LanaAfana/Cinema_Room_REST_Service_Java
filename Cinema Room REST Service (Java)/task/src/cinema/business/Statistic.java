package cinema.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {

    @JsonProperty("current_income")
    int currentIncome;

    @JsonProperty("number_of_available_seats")
    int numberOfAvailableSeats;

    @JsonProperty("number_of_purchased_tickets")
    int numberOfPurchasedTickets;

}
