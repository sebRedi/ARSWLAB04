    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package edu.eci.arsw.blueprints.services;

    import edu.eci.arsw.blueprints.filters.Filter;
    import edu.eci.arsw.blueprints.model.Blueprint;
    import edu.eci.arsw.blueprints.model.Point;
    import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
    import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
    import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

    import java.util.HashSet;
    import java.util.LinkedHashMap;
    import java.util.Map;
    import java.util.Set;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.stereotype.Service;

    /**
     *
     * @author hcadavid
     */
    @Service
    public class BlueprintsServices {

        private final BlueprintsPersistence bpp;
        /**
         * A) 'redundancyFilter' para filtrado de redundancias
         * B) 'undersamplingFilter' para filtrado de submuestreo
         */
        @Qualifier("redundancyFilter")
        private final Filter filter;

        @Autowired
        public BlueprintsServices(BlueprintsPersistence bpp, Filter filter){
            this.bpp = bpp;
            this.filter = filter;
        }

        /**
         * Add a blueprint to the repository
         * @param bp blueprint to add
         */
        public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
            bpp.saveBlueprint(bp);
        }

        /**
         * Gets a copy of all existing blueprints
         * @return A java HashSet with all existing blueprints
         */
        public Set<Blueprint> getAllBlueprints(){
            //return new HashSet<>(bpp.getAllBlueprints());
            Set<Blueprint> originals = bpp.getAllBlueprints();
            Set<Blueprint> processed = new HashSet<>();

            for (Blueprint bp : originals) {
                Blueprint filtered = filter.applyFilter(bp);
                processed.add(filtered);
            }

            return processed;
        }

        /**
         * Get a blueprint by author and specified name
         * @param author blueprint's author
         * @param name blueprint's name
         * @return the blueprint of the given name created by the given author
         * @throws BlueprintNotFoundException if there is no such blueprint
         */
        public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
            return filter.applyFilter(bpp.getBlueprint(author, name));
        }

        /**
         * Get all blueprints of a specified author
         * @param author blueprint's author
         * @return all the blueprints of the given author
         * @throws BlueprintNotFoundException if the given author doesn't exist
         */
        public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
            Set<Blueprint> authored = bpp.getBlueprintsByAuthor(author);
            Set<Blueprint> result = new HashSet<>();

            for (Blueprint bp : authored) {
                result.add(filter.applyFilter(bp));
            }

            return result;
        }

        public void updateBlueprint(String author, String name, Blueprint updatedBlueprint) throws BlueprintNotFoundException {
            Blueprint existing = bpp.getBlueprint(author, name);
            if (existing == null) {
                throw new BlueprintNotFoundException("Blueprint not found with author " + author + " and name " + name);
            }
            existing.setPoints(updatedBlueprint.getPoints());
        }

    }
