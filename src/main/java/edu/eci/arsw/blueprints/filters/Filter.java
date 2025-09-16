package edu.eci.arsw.blueprints.filters;

import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.beans.factory.annotation.Qualifier;

public interface Filter{
    Blueprint applyFilter(Blueprint bp);
}
