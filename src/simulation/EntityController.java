package simulation;

import entities.Brain;
import entities.Entity;
import math.Vector2D;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EntityController {
    private ArrayList<Entity> entityList = new ArrayList<>();
    private EnvironmentController envController;
    private Map2D map2D;
    private boolean running = true;
    private int generationAge = 0;
    private int maxGenerationAge = 2000;

    public EntityController(EnvironmentController envController){
        this.envController = envController;
        map2D = envController.getMap();
    }

    public void createInitialEntities(int amount)
    {
        int offset = Simulation.BLOCKSIZE /2;
        double x = map2D.getStart().getX() + offset;
        double y =  map2D.getStart().getY() + offset;

        Vector2D startPos = new Vector2D(x,y);

        for(int i = 0; i < amount; i++)
            entityList.add(new Entity(startPos));
    }

    public ArrayList<Entity> selectParentsOfPopulation(ArrayList<Entity> population, int amount){
        ArrayList<Entity> parents = new ArrayList<>();
        for(Entity e : population){
            double distance = getDirectDistance(e.getPosition(), map2D.getGoal());
            if(parents.size() < amount){
                parents.add(e);
            }else{
                //get worst parent
                Entity worstParent = parents.get(0);
                double worstDistance = getDirectDistance(parents.get(0).getPosition(), map2D.getGoal());
                for(Entity parent : parents){
                    double parentDistance = getDirectDistance(parent.getPosition(), map2D.getGoal());
                    if(parentDistance > worstDistance){
                        worstParent = parent;
                        worstDistance = parentDistance;
                    }
                }
                //compare
                if(distance < worstDistance){
                    parents.remove(worstParent);
                    parents.add(e);
                }
            }
        }

        return parents;
    }

    public void createEntitiesByParents(ArrayList<Entity> parents, int offspringAmount, double mutationProb){
        this.entityList = new ArrayList<>();
        ArrayList<Brain> parentBrains = new ArrayList<>();
        for(Entity p : parents){
            parentBrains.add(p.getBrain().copy());
        }

        int offset = Simulation.BLOCKSIZE /2;
        double x = map2D.getStart().getX() + offset;
        double y =  map2D.getStart().getY() + offset;
        Vector2D startPos = new Vector2D(x,y);

        for(Brain pb : parentBrains){
            for(int i=0; i<offspringAmount-1; i++){
                Entity e = new Entity(startPos);
                e.setBrain(pb.copy());
                e.getBrain().getRoot().setEnt(e);
                e.getBrain().mutate(mutationProb);
                this.entityList.add(e);
            }
            Entity parentCopy = new Entity(startPos);
            parentCopy.setBrain(pb.copy());
            parentCopy.getBrain().getRoot().setEnt(parentCopy);
            parentCopy.setColor(Color.green);
            this.entityList.add(parentCopy);
        }
    }

    public void update()
    {
        if(generationAge >= maxGenerationAge){
            //evaluate and reset
            Simulation.running = false;

            //bla bla new population
            ArrayList<Entity> parents = selectParentsOfPopulation(entityList, 10);
            createEntitiesByParents(parents, 10, 0.01);
            generationAge = 0;
            Simulation.running = true;
        }else{
            for (Entity entity: entityList) {
                entity.move(map2D);
            }
            generationAge++;
        }
    }

    private Entity getBestEntity(){
        Vector2D goal = map2D.getGoal();
        double bestDistance = Double.MAX_VALUE;
        Entity best = null;
        for(Entity e : entityList){
            double distance = getDirectDistance(e.getPosition(), goal);
            if(distance < bestDistance){
                bestDistance = distance;
                best = e;
            }
        }
        if(best == null){
            System.out.println("no one made it, calc distance!");
            for(Entity e : entityList){
                double distance = e.getPosition().getDistance(goal);
                if(distance < bestDistance){
                    bestDistance = distance;
                    best = e;
                }
            }
        }
        return best;
    }

    private double getDirectDistance(Vector2D position1, Vector2D position2){
        double distance = position1.getDistance(position2);
        Vector2D direction = position2.clone().sub(position1).normalize();
        Vector2D nextPos = position1.clone();

        for(int i=1; i<distance;i++){
            nextPos.add(direction);
            int mapPositionX = (int) nextPos.getX() / Simulation.BLOCKSIZE;
            int mapPositionY = (int) nextPos.getY() / Simulation.BLOCKSIZE;
            int blockValue = map2D.getValueOf(mapPositionX, mapPositionY);
            //System.out.println(nextPos + " -> " + mapPositionX + "|" + mapPositionY + " -> " + blockValue);
            if(blockValue == Map2D.BORDER){
                return Double.MAX_VALUE;
            }
        }
        return distance;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(ArrayList<Entity> entityList) {
        this.entityList = entityList;
    }


}
