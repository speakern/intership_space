package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipDTO;
import com.space.model.ShipType;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ShipService {
    Ship getShipById(Long id);
    Ship saveShip(Ship ship);
    Ship updateShip(Long id, ShipDTO ship);
    void deleteShip(Long id);
    List<Ship> findAll(String name, String planet, ShipType shiptype, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize);
    Integer count(String name, String planet, ShipType shiptype, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
}