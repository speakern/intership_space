package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;
import java.sql.Date;
import java.util.Calendar;

public class ShipSpecs {
    public static Specification<Ship> nameContains(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(root.get("name"), "%" + name + "%");
    }
    public static Specification<Ship> planetContains(String planet) {
        return (root, query, cb) -> planet == null ? null : cb.like(root.get("planet"), "%" + planet + "%");
    }
    public static Specification<Ship> withShipType(ShipType shipType) {
        return (root, query, cb) -> shipType == null ? null : cb.equal(root.get("shipType"), shipType);
    }
    public static Specification<Ship> withAfterMore(Long after) {
        if (after != null) {
            Long prodDate = makeOnlyYear(after);
            System.out.println("after: " + prodDate);
            return (root, query, cb) -> prodDate == null ? null : cb.greaterThanOrEqualTo(root.get("prodDate"), new Date(prodDate));
        }
        return null;
    }
    public static Specification<Ship> withBeforeLess(Long before) {
        if (before != null) {
            Long prodDate = makeOnlyYear(before);
            System.out.println("before: " + prodDate);
            return (root, query, cb) -> prodDate == null ? null : cb.lessThanOrEqualTo(root.get("prodDate"), new Date(prodDate));
        }
        return null;
    }
    public static Specification<Ship> isUsed(Boolean isUsed) {
        return (root, query, cb) -> isUsed == null ? null : cb.equal(root.get("isUsed"), isUsed);
    }

    public static Specification<Ship> withMinSpeed(Double minSpeed) {
        return (root, query, cb) -> minSpeed == null ? null : cb.greaterThanOrEqualTo(root.get("speed"), minSpeed);
    }

    public static Specification<Ship> withMaxSpeed(Double maxSpeed) {
        return (root, query, cb) -> maxSpeed == null ? null : cb.lessThanOrEqualTo(root.get("speed"), maxSpeed);
    }

    public static Specification<Ship> withMinCrewSize(Integer minCrewSize) {
        return (root, query, cb) -> minCrewSize == null ? null : cb.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
    }

    public static Specification<Ship> withMaxCrewSize(Integer maxCrewSize) {
        return (root, query, cb) -> maxCrewSize == null ? null : cb.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
    }

    public static Specification<Ship> withMinRating(Double minRating) {
        return (root, query, cb) -> minRating == null ? null : cb.greaterThanOrEqualTo(root.get("rating"), minRating);
    }

    public static Specification<Ship> withMaxRating(Double maxRating) {
        return (root, query, cb) -> maxRating == null ? null : cb.lessThanOrEqualTo(root.get("rating"), maxRating);
    }


    public static Long makeOnlyYear(Long date) {
        Calendar calendar = Calendar.getInstance(/*TimeZone.getTimeZone("UTC")*/);
        calendar.setTimeInMillis(date);
        System.out.println("год: " + calendar.get(Calendar.YEAR));
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

}
