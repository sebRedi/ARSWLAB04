package edu.eci.arsw.blueprints.test.persistence.services;

import edu.eci.arsw.blueprints.filters.Filter;
import edu.eci.arsw.blueprints.filters.impl.RedundancyFilter;
import edu.eci.arsw.blueprints.filters.impl.UndersamplingFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class BlueprintsServicesTest {

    @Test
    public void testGetAllBlueprintsWithRedundancyFilter() throws Exception {
        InMemoryBlueprintPersistence repo = new InMemoryBlueprintPersistence();
        BlueprintsServices service = new BlueprintsServices(repo, new RedundancyFilter());

        // Plano con puntos duplicados
        Blueprint bp = new Blueprint("thor", "martillo",
                new Point[]{ new Point(1, 1), new Point(1, 1), new Point(2, 2) });
        repo.saveBlueprint(bp);

        Set<Blueprint> result = service.getAllBlueprints();
        Blueprint filtered = result.iterator().next();

        assertEquals("El filtro debería eliminar puntos duplicados", 2, filtered.getPoints().size());
    }

    @Test
    public void testGetBlueprintsByAuthorWithUnderamplingFilter() throws Exception {
        InMemoryBlueprintPersistence memory = new InMemoryBlueprintPersistence();
        Filter undersampling = new UndersamplingFilter(); // filtro real
        BlueprintsServices services = new BlueprintsServices(memory, undersampling);

        // Creamos un plano con 4 puntos
        Blueprint bp = new Blueprint("ironman", "armadura", new Point[]{
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3)
        });
        services.addNewBlueprint(bp);

        Set<Blueprint> result = services.getBlueprintsByAuthor("ironman");
        assertEquals("Debería haber un único plano para el autor", 1, result.size());

        Blueprint filtered = result.iterator().next();
        assertEquals("El filtro de submuestreo debería dejar 2 puntos", 2, filtered.getPoints().size());

        // Verificamos los puntos restantes por coordenadas
        Point first = filtered.getPoints().get(0);
        Point second = filtered.getPoints().get(1);

        assertEquals(0, first.getX());
        assertEquals(0, first.getY());

        assertEquals(2, second.getX());
        assertEquals(2, second.getY());
    }


    @Test
    public void testGetBlueprintWithFilter() throws Exception {
        InMemoryBlueprintPersistence repo = new InMemoryBlueprintPersistence();
        BlueprintsServices service = new BlueprintsServices(repo, new RedundancyFilter());

        // Plano con duplicados
        Blueprint bp = new Blueprint("hulk", "puño",
                new Point[]{ new Point(5, 5), new Point(5, 5), new Point(10, 10) });
        repo.saveBlueprint(bp);

        Blueprint filtered = service.getBlueprint("hulk", "puño");

        assertEquals("El filtro debería devolver solo 2 puntos", 2, filtered.getPoints().size());
    }
}
