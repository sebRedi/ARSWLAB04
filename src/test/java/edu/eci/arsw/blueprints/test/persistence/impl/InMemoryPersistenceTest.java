/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {

    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);

        ibpp.saveBlueprint(bp0);

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        ibpp.saveBlueprint(bp);

        assertNotNull("Loading a previously stored blueprint returned null.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()));

        assertEquals("Loading a previously stored blueprint returned a different blueprint.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp);

    }

    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaint",pts2);

        try{
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        }
        catch (BlueprintPersistenceException ex){

        }


    }

    @Test
    public void shouldReturnAllBlueprintsByAuthor() throws Exception {
        InMemoryBlueprintPersistence storage = new InMemoryBlueprintPersistence();

        Point[] pointsA = { new Point(5, 5), new Point(10, 10) };
        Blueprint house = new Blueprint("sebastian", "plano001", pointsA);
        storage.saveBlueprint(house);

        Point[] pointsB = { new Point(15, 15), new Point(20, 20) };
        Blueprint apartment = new Blueprint("sebastian", "plano002", pointsB);
        storage.saveBlueprint(apartment);

        Set<Blueprint> result = storage.getBlueprintsByAuthor("sebastian");

        assertEquals("El autor debería tener exactamente dos planos registrados",
                2, result.size());
        assertTrue("El conjunto debe contener los planos guardados",
                result.contains(house) && result.contains(apartment));
    }

    @Test(expected = BlueprintNotFoundException.class)
    public void shouldThrowExceptionWhenAuthorDoesNotExist() throws Exception {
        InMemoryBlueprintPersistence storage = new InMemoryBlueprintPersistence();
        storage.getBlueprintsByAuthor("autorInexistente");
    }

    @Test
    public void shouldReturnAllStoredBlueprints() throws Exception {
        InMemoryBlueprintPersistence repository = new InMemoryBlueprintPersistence();

        Blueprint plan1 = new Blueprint("marco", "plano003", new Point[]{ new Point(1, 1), new Point(2, 2) });
        Blueprint plan2 = new Blueprint("polo", "plano004", new Point[]{ new Point(3, 3), new Point(4, 4) });

        repository.saveBlueprint(plan1);
        repository.saveBlueprint(plan2);

        Set<Blueprint> allBlueprints = repository.getAllBlueprints();

        assertTrue("El conjunto de planos debe contener el primero guardado", allBlueprints.contains(plan1));
        assertTrue("El conjunto de planos debe contener el segundo guardado", allBlueprints.contains(plan2));
    }

    @Test
    public void shouldRetrieveBlueprintByAuthorAndName() throws Exception {
        InMemoryBlueprintPersistence storage = new InMemoryBlueprintPersistence();

        Point[] coordinates = { new Point(7, 7), new Point(14, 14) };
        Blueprint customPlan = new Blueprint("goku", "planEspecial", coordinates);

        storage.saveBlueprint(customPlan);

        Blueprint fetched = storage.getBlueprint("goku", "planEspecial");

        assertNotNull("El blueprint recuperado no debería ser nulo", fetched);
        assertEquals("El blueprint recuperado debería ser igual al que se guardó", customPlan, fetched);
        assertArrayEquals("Los puntos deberían coincidir exactamente",
                customPlan.getPoints().toArray(), fetched.getPoints().toArray());
    }



}
