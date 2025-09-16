/**package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {

    public static void main(String[] args) throws Exception {
        // Inicialización del contexto de Spring
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BlueprintsServices blueprintService = context.getBean(BlueprintsServices.class);

        // Definición de planos iniciales
        Blueprint planoPuente = new Blueprint("marco", "puente", new Point[]{
                new Point(0, 0), new Point(10, 10)
        });
        Blueprint planoCarretera = new Blueprint("marco", "carretera", new Point[]{
                new Point(5, 5), new Point(15, 15)
        });
        Blueprint planoUniversidad = new Blueprint("polo", "universidad", new Point[]{
                new Point(20, 20), new Point(30, 30)
        });

        // Registro en el servicio
        blueprintService.addNewBlueprint(planoPuente);
        blueprintService.addNewBlueprint(planoCarretera);
        blueprintService.addNewBlueprint(planoUniversidad);

        // Consulta individual
        System.out.println("=== Consulta individual ===");
        Blueprint recuperado = blueprintService.getBlueprint("marco", "puente");
        System.out.println("Resultado -> " + recuperado);

        // Consulta por autor
        System.out.println("\n=== Planos registrados por 'marco' ===");
        for (Blueprint bp : blueprintService.getBlueprintsByAuthor("marco")) {
            System.out.println("-> " + bp);
        }

        // Consulta global
        System.out.println("\n=== Listado completo de planos ===");
        blueprintService.getAllBlueprints().forEach(bp -> {
            System.out.println("Plano disponible: " + bp);
        });
    }
}
*/