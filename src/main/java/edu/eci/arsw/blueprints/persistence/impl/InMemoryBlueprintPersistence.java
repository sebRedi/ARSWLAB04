package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.HashSet;

/**
 *
 * @author hcadavid
 */
@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final ConcurrentMap<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        Point[] pts = new Point[]{new Point(140, 140), new Point(115, 115)};
        Blueprint bp = new Blueprint("_authorname_", "_bpname_", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);

        Point[] pts2 = new Point[]{new Point(10, 10), new Point(20, 20), new Point(30, 30)};
        Blueprint bp2 = new Blueprint("Sebastian", "Plano1", pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);

        Point[] pts3 = new Point[]{new Point(5, 5), new Point(15, 15)};
        Blueprint bp3 = new Blueprint("Sebastian", "Plano2", pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);

        Point[] pts4 = new Point[]{new Point(100, 50), new Point(120, 80), new Point(140, 100)};
        Blueprint bp4 = new Blueprint("Vegueta", "Plano3", pts4);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Tuple<String, String> key = new Tuple<>(bp.getAuthor(), bp.getName());
        // operación atómica: si ya existe, no lo reemplaza
        if (blueprints.putIfAbsent(key, bp) != null) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        Blueprint bp = blueprints.get(new Tuple<>(author, bprintname));
        if (bp == null) {
            throw new BlueprintNotFoundException("Blueprint not found: " + author + " - " + bprintname);
        }
        return bp;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(blueprints.values());
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> result = blueprints.entrySet().stream()
                .filter(e -> author.equals(e.getKey().getElem1()))
                .map(java.util.Map.Entry::getValue)
                .collect(Collectors.toSet());

        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints found for the specified author: " + author);
        }
        return result;
    }

    public void updateBlueprint(String author, String name, Blueprint newBp) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, name);

        Blueprint updated = blueprints.computeIfPresent(key, (k, oldBp) -> newBp);

        if (updated == null) {
            throw new BlueprintNotFoundException("Cannot update. Blueprint not found: " + author + " - " + name);
        }
    }
}
