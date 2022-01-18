package simulation;

import entities.Entity;
import math.Vector2D;

import java.util.ArrayList;

public class EntityController {
    private ArrayList<Entity> entityList = new ArrayList<>();
    private EnvironmentController envController;
    private Map2D map2D;

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
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(ArrayList<Entity> entityList) {
        this.entityList = entityList;
    }


}
