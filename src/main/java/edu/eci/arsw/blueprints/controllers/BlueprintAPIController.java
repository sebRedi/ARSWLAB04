/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping("/blueprints")
public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices blueprintsServices;

    @GetMapping
    public ResponseEntity<Set<Blueprint>> getAllBlueprints() {
        try {
            Set<Blueprint> allBlueprints = blueprintsServices.getAllBlueprints();
            return ResponseEntity.ok(allBlueprints);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<Set<Blueprint>> getBlueprintsByAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> blueprints = blueprintsServices.getBlueprintsByAuthor(author);
            if (blueprints == null || blueprints.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(blueprints);
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<Blueprint> getBlueprintByAuthorAndName(@PathVariable("author") String author,
                                                                 @PathVariable("bpname") String bpname) {
        try {
            Blueprint blueprint = blueprintsServices.getBlueprint(author, bpname);
            if (blueprint == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(blueprint);
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlueprint(@RequestBody Blueprint blueprint) {
        try {
            blueprintsServices.addNewBlueprint(blueprint);
            return ResponseEntity.status(HttpStatus.CREATED).body(blueprint);
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el plano");
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(
            @PathVariable String author,
            @PathVariable String bpname,
            @RequestBody Blueprint updatedBlueprint) {
        try {
            blueprintsServices.updateBlueprint(author, bpname, updatedBlueprint);
            return ResponseEntity.ok("Blueprint updated successfully");
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(404).body("Blueprint not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating blueprint: " + e.getMessage());
        }
    }

}

