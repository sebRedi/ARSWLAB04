package edu.eci.arsw.blueprints.filters.impl;

import edu.eci.arsw.blueprints.filters.Filter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Primary
@Component("redundancyFilter")
public class RedundancyFilter implements Filter {

    @Override
    public Blueprint applyFilter(Blueprint blueprint) {
        List<Point> uniquePoints = new LinkedList<>();
        Point lastPoint = null;

        for (Point current : blueprint.getPoints()) {
            if (lastPoint == null || (lastPoint.getX() != current.getX() || lastPoint.getY() != current.getY())) {
                uniquePoints.add(current);
            }
            lastPoint = current;
        }

        Point[] resultArray = uniquePoints.toArray(new Point[uniquePoints.size()]);
        return new Blueprint(blueprint.getAuthor(), blueprint.getName(), resultArray);
    }
}
