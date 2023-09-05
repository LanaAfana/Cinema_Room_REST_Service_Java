package cinema.presentation;

import cinema.business.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CinemaRoomController {

    private final CinemaRoom room;

    @Autowired
    CinemaRoomController(CinemaRoom cinemaRoom) { this.room = cinemaRoom; }

    @GetMapping("/seats")
    public CinemaRoom getSeats() {
        return room;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password) throws JsonProcessingException {
        if (password == null || !password.equals("super_secret")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ObjectMapper()
                            .writeValueAsString(new SeatError("The password is wrong!"))
                    );
        } else {
            return ResponseEntity
                    .ok(room.getStatistic());
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> postPurchase(@RequestBody SeatNumber seatNumber) throws JsonProcessingException {
        if (seatNumber.getRow() < 1 || seatNumber.getRow() > room.getTotalRows() ||
                seatNumber.getColumn() < 1 || seatNumber.getColumn() > room.getTotalColumns()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectMapper()
                            .writeValueAsString(new SeatError("The number of a row or a column is out of bounds!"))
                    );
        }

        if (room.getSeats().get(seatNumber.getRow() - 1).get(seatNumber.getColumn() - 1).isAvailable()) {
            return ResponseEntity.ok(room.doPurchase(seatNumber.getRow() - 1, seatNumber.getColumn() - 1));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new ObjectMapper()
                            .writeValueAsString(new SeatError("The ticket has been already purchased!")));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> postReturn(@RequestBody Token token) throws JsonProcessingException {
        Seat seat = room.doRefund(token.getToken());

        if (seat != null) {
            return ResponseEntity
                    .ok(new ReturnedTicket(seat));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new Gson()
                            .toJson(new SeatError("Wrong token!")));
        }
    }
}
