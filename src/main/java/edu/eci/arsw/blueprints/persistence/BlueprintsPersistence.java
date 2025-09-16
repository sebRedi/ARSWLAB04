/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;

import java.util.Set;

/**
 *
 * @author hcadavid
 */
public interface BlueprintsPersistence {

    /**
     * Get a set with all existing blueprints
     * @return All blueprints in a java Set
     */
    public Set<Blueprint> getAllBlueprints();

    /**
     * Get a set with all blueprints filtered by author
     * @param author Blueprint's author in String
     * @return All blueprints filtered by author in a java Set
     * @throws BlueprintNotFoundException If the provided author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
    
    /**
     * 
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    
    /**
     * 
     * @param author blueprint's author
     * @param bprintname blueprint's author
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String bprintname) throws BlueprintNotFoundException;
    
}
