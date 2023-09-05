package cinema.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private int totalIncome;

    public CinemaRoom() {
        this.totalRows = 9;
        this.totalColumns = 9;
        this.seats = createSeats();
        this.renewStatistic();
    }

    public CinemaRoom (int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.seats = createSeats();
        this.renewStatistic();
    }

    private List<List<Seat>> createSeats() {
        List<List<Seat>> rows = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            List<Seat> row = new ArrayList<>();
            for (int j = 0; j < totalColumns; j++) {
                row.add(new Seat(i + 1, j + 1));
            }
            rows.add(row);
        }
        return rows.stream().toList();
    }

    private void renewStatistic() {
        List<Seat> seats = new ArrayList<>();
        int income = 0;
        for (int i = 0; i < this.totalRows; i++) {
            for (int j = 0; j < this.totalColumns; j++) {
                if (this.seats.get(i).get(j).isAvailable())
                    seats.add(this.seats.get(i).get(j));
                else
                    income += this.seats.get(i).get(j).getPrice();
            }
        }
        this.setTotalIncome(income);
        this.setAvaliableSeats(seats.stream().toList());
    }

    public Ticket doPurchase(int row, int column) {
        Seat seat = this.seats.get(row).get(column);
        seat.setAvailable(false);
        seat.setToken(UUID.randomUUID().toString());
        this.renewStatistic();
        return new Ticket(seat.getToken(), seat);
    }

    public Seat doRefund(String token) {
        for (int i = 0; i < this.totalRows; i++) {
            for (int j = 0; j < this.totalColumns; j++) {
                Seat seat = this.seats.get(i).get(j);
                if (seat.getToken().equals(token)) {
                    seat.setAvailable(true);
                    seat.setToken("");
                    this.renewStatistic();
                    return seat;
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public Statistic getStatistic() {
        return new Statistic(this.getTotalIncome(),
                this.getAvaliableSeats().size(),
                this.getTotalRows() * this.getTotalColumns() - this.getAvaliableSeats().size());
    }
}
