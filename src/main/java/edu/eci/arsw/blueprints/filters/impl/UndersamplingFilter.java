package edu.eci.arsw.blueprints.filters.impl;

import edu.eci.arsw.blueprints.filters.Filter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;

@Component("undersamplingFilter")
public class UndersamplingFilter implements Filter {

    @Override
    public Blueprint applyFilter(Blueprint original) {
        List<Point> sampledPoints = new LinkedList<>();
        List<Point> allPoints = original.getPoints();

        int index = 0;
        for (Point p : allPoints) {
            if (index % 2 == 0) {
                sampledPoints.add(p);
            }
            index++;
        }

        Point[] finalSet = sampledPoints.toArray(new Point[sampledPoints.size()]);
        return new Blueprint(original.getAuthor(), original.getName(), finalSet);
    }
}

