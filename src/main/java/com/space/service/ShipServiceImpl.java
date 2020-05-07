package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipDTO;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.space.service.ShipSpecs.*;
import static com.space.utils.ShipHelper.*;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository repository;

    @Autowired
    public void setShipRepository(ShipRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ship saveShip(Ship ship) {
        return repository.save(ship);
    }

    @Override
    public void deleteShip(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Ship updateShip(Long id, ShipDTO ship) {

        if (repository.findById(id).isPresent()) {
            Ship updated = repository.findById(id).get();
            if (ship.getName() != null) updated.setName(ship.getName());
            if (ship.getPlanet() != null ) updated.setPlanet(ship.getPlanet());
            if (ship.getShipType() != null) updated.setShipType(ship.getShipType());
            if (ship.getProdDate() != null) updated.setProdDate(new Date(ship.getProdDate()));
            if (ship.getUsed() != null) updated.setUsed(ship.getUsed());
            if (ship.getSpeed() != null) updated.setSpeed(round(ship.getSpeed(), 2));
            if (ship.getCrewSize() != null) updated.setCrewSize(ship.getCrewSize());
            updated.setRating(round(getRating(updated.getProdDate().getTime(), updated.getUsed(), updated.getSpeed()),2));
            return repository.save(updated);
        } else {
            return null;
        }
    }

    @Override
    public Ship getShipById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public List<Ship> findAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order, Integer pageNumber, Integer pageSize) {

        Specification<Ship> specification = getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        if (order ==null) order = ShipOrder.ID;
        Pageable sortedByName =
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        Page<Ship> pages = repository.findAll(specification, sortedByName);

        List<Ship> shipList = new ArrayList<Ship>();
        if(pages != null && pages.hasContent()) {
            shipList = pages.getContent();
        }
        return shipList;
    }

    @Override
    public Integer count(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        Specification<Ship> specification = getSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return Integer.valueOf((int)repository.count(specification));
    }

    private Specification<Ship> getSpecification(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        return Specification
                .where(nameContains(name))
                .and(planetContains(planet))
                .and(withShipType(shipType))
                .and(withAfterMore(after))
                .and(withBeforeLess(before))
                .and(isUsed(isUsed))
                .and(withMinSpeed(minSpeed))
                .and(withMaxSpeed(maxSpeed))
                .and(withMinCrewSize(minCrewSize))
                .and(withMaxCrewSize(maxCrewSize))
                .and(withMinRating(minRating))
                .and(withMaxRating(maxRating));
    }

}
