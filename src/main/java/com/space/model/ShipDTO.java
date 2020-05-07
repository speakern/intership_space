package com.space.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

public class ShipDTO {

    private Long id;
    private String name;
    private String planet;
    private ShipType shipType;
    private Long prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private Double rating;

    public ShipDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Long getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}
