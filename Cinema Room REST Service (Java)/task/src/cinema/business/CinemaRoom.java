package cinema.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Component
public class CinemaRoom {
    @JsonProperty("total_rows")
    private int totalRows;
    @JsonProperty("total_columns")
    private int totalColumns;
    @JsonIgnore
    private List<List<Seat>> seats;
    @JsonProperty("available_seats")
    private List<Seat> avaliableSeats;

    public CinemaRoom() {
        this.totalRows = 9;
        this.totalColumns = 9;

        List<List<Seat>> rows = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            List<Seat> row = new ArrayList<>();
            for (int j = 0; j < totalColumns; j++) {
                row.add(new Seat(i + 1, j + 1));
            }
            rows.add(row);
        }
        this.seats = rows.stream().toList();
        this.avaliableSeats = setAvaliableSeats();
    }

    public CinemaRoom (int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;

        List<List<Seat>> rows = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            List<Seat> row = new ArrayList<>();
            for (int j = 0; j < totalColumns; j++) {
                row.add(new Seat(i + 1, j + 1));
            }
            rows.add(row);
        }
        this.seats = rows.stream().toList();
        this.avaliableSeats = setAvaliableSeats();
    }

    private List<Seat> setAvaliableSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < this.totalRows; i++) {
            for (int j = 0; j < this.totalColumns; j++) {
                if (this.seats.get(i).get(j).isAvailable())
                    seats.add(this.seats.get(i).get(j));
            }
        }
        return seats.stream().toList();
    }

    public Ticket doPurchase(int row, int column) {
        Seat seat = this.seats.get(row).get(column);
        seat.setAvailable(false);
        seat.setToken(UUID.randomUUID().toString());
        this.avaliableSeats = setAvaliableSeats();
        return new Ticket(seat.getToken(), seat);
    }

    public Seat seatByToken(String token) {
        for (int i = 0; i < this.totalRows; i++) {
            for (int j = 0; j < this.totalColumns; j++) {
                Seat seat = this.seats.get(i).get(j);
                if (seat.getToken().equals(token)) {
                    seat.setAvailable(true);
                    seat.setToken("");
                    return seat;
                }
            }
        }
        return null;
    }
}
