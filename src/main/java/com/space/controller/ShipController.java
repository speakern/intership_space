package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipDTO;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.space.utils.ShipHelper.*;

@RestController
@Validated
@RequestMapping("/rest")
public class ShipController {
    private ShipService service;

    @Autowired
    public void setShipService(ShipService service) {
        this.service = service;
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable String id) {
        try {
            Long idLong = validId(id);
            Ship ship = service.getShipById(idLong);
            if (ship != null) {
                service.deleteShip(idLong);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/ships")
    ResponseEntity<Ship>  newShip(@RequestBody ShipDTO ship){

        if (ship.getName() == null || ship.getName().equals("") ||
                ship.getPlanet() == null || ship.getPlanet().equals("") ||
                ship.getShipType() == null ||
                ship.getSpeed() == null ||
                ship.getCrewSize() == null ||
                !validShipDTO(ship)
        ) {
            return ResponseEntity.badRequest().build();
        }

        if (ship.getUsed() == null) ship.setUsed(false);

        Ship newShip = new Ship(ship.getName(), ship.getPlanet(), ship.getShipType(), new Date(ship.getProdDate()),
                ship.getUsed(), round(ship.getSpeed(), 2), ship.getCrewSize(), round(getRating(ship.getProdDate(), ship.getUsed(), ship.getSpeed()), 2));
        return ResponseEntity.ok(service.saveShip(newShip));
    }

    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> updateShip(@PathVariable String id, @RequestBody ShipDTO ship) {
        try {
            Long idLong = validId(id);
            if (validShipDTO(ship)) {
                Ship updatedShip = service.updateShip(idLong, ship);
                if (updatedShip != null) {
                    return ResponseEntity.ok().body(updatedShip);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (NumberFormatException e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable String id) {
        try {
            Long idLong = validId(id);
            Ship ship = service.getShipById(idLong);
                if (ship != null) {
                    return ResponseEntity.ok().body(ship);
                } else {
                    return ResponseEntity.notFound().build();
                }
        } catch (NumberFormatException e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/ships")
    public List<Ship> getAllQueries(@RequestParam(value="name", required=false) String name,
                                    @RequestParam(value="planet", required=false) String planet,
                                    @RequestParam(value="shipType", required=false ) ShipType shiptype,
                                    @RequestParam(value="after", required=false ) Long after,
                                    @RequestParam(value="before", required=false) Long before,
                                    @RequestParam(value="isUsed", required=false) Boolean isUsed,
                                    @RequestParam(value="minSpeed", required=false) Double minSpeed,
                                    @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
                                    @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
                                    @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
                                    @RequestParam(value="minRating", required=false) Double minRating,
                                    @RequestParam(value="maxRating", required=false) Double maxRating,
                                    @RequestParam(value="order", required=false) ShipOrder order,
                                    @RequestParam(value="pageNumber", required=false, defaultValue="0") Integer pageNumber,
                                    @RequestParam(value="pageSize", required=false, defaultValue="3") Integer pageSize)
    {
        List<Ship> result = service.findAll(name, planet, shiptype, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);
        return result;
    }


    @GetMapping(value = "/ships/count")
    public Integer getAllQueries(@RequestParam(value="name", required=false) String name,
                                    @RequestParam(value="planet", required=false) String planet,
                                    @RequestParam(value="shipType", required=false ) ShipType shiptype,
                                    @RequestParam(value="after", required=false ) Long after,
                                    @RequestParam(value="before", required=false) Long before,
                                    @RequestParam(value="isUsed", required=false) Boolean isUsed,
                                    @RequestParam(value="minSpeed", required=false) Double minSpeed,
                                    @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
                                    @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
                                    @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
                                    @RequestParam(value="minRating", required=false) Double minRating,
                                    @RequestParam(value="maxRating", required=false) Double maxRating)
    {
        return service.count(name, planet, shiptype, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    private static long validId(String id) throws NumberFormatException{
        long result = 0;
        if (Pattern.matches("\\d+", id)) {
            result = Long.parseLong(id);
            if (result > 0) {
                return result;
            }
        }
        throw new NumberFormatException();
    }

    private static boolean validShipDTO(ShipDTO ship) {
        int year = 0;
        if (ship.getProdDate() != null) {
            year = yearFromDate(ship.getProdDate());
        }
        Double speed = 0.0;
        if (ship.getSpeed() != null) {
            speed = round(ship.getSpeed(), 2);
        }
        if ( (ship.getProdDate() != null) && (ship.getProdDate() < 0) ||
                (ship.getProdDate() != null) && (year < 2800) ||
                (ship.getProdDate() != null) && (year > 3019) ||
                (ship.getSpeed() != null) && (speed < 0.01) ||
                (ship.getSpeed() != null) && (speed > 0.99) ||
                (ship.getCrewSize() != null) && (ship.getCrewSize() < 1) ||
                (ship.getCrewSize() != null) && (ship.getCrewSize() > 9999) ||
                (ship.getName() != null) && (ship.getName().length() > 50) ||
                (ship.getPlanet() != null) && (ship.getPlanet().length() > 50) ||
                (ship.getName() != null) && (ship.getName().length() == 0) ||
                (ship.getPlanet() != null) && (ship.getPlanet().length() == 0))
        {
            return false;
        }
        return true;
    }

}
