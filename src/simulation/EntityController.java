package simulation;

import entities.Entity;
import math.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class EntityController {
    private ArrayList<Entity> entityList = new ArrayList<>();
    private EnvironmentController envController;
    private Map2D map2D;
    private int generationAge = 0;
    private int maxGenerationAge = 1000;

    public EntityController(EnvironmentController envController){
        this.envController = envController;
        map2D = envController.getMap();
    }

    public void createEntities(int amount)
    {
        int offset = Simulation.BLOCKSIZE /2;
        double x = map2D.getStart().getX() + offset;
        double y =  map2D.getStart().getY() + offset;

        Vector2D startPos = new Vector2D(x,y);

        for(int i = 0; i < amount; i++)
            entityList.add(new Entity(startPos));
    }

    public void update()
    {
        for (Entity entity: entityList) {
            entity.move(map2D);
        }
        generationAge++;
        if(generationAge >= maxGenerationAge){
            //evaluate and reset
            Entity e = this.getBestEntity();
            e.setColor(Color.GREEN);
        }
    }

    private Entity getBestEntity(){
        Vector2D goal = map2D.getGoal();
        double bestDistance = entityList.get(0).getPosition().getDistance(goal);
        Entity best = entityList.get(0);
        for(Entity e : entityList){
            double distance = e.getPosition().getDistance(goal);
            if(distance < bestDistance){
                bestDistance = distance;
                best = e;
            }
        }
        return best;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(ArrayList<Entity> entityList) {
        this.entityList = entityList;
    }


}
