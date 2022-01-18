package entities;

import math.Vector2D;
import simulation.EntityController;
import simulation.Map2D;
import simulation.Simulation;

import java.awt.*;

public class Entity {

    private Vector2D position;
    private Vector2D direction;
    private double speed = 1.0f;
    private Brain brain;

    private double distance = 8;


    public Entity(Vector2D startPos)
    {
        this.position = new Vector2D(startPos.getX(), startPos.getY());
        this.direction = new Vector2D(Math.random() -0.5f, Math.random() -0.5f).mult(distance);
        this.brain = new Brain(this);
    }


    public void move(Map2D map){

        //Update Position and direction Vector
        double oldX = position.getX();
        double oldY = position.getY();

        this.position.add(this.brain.getDirectionVector());
    }

    public void draw(Graphics g){
        Vector2D pos = this.getPosition();
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        int r = 10;
        x = x-(r/2);
        y = y-(r/2);
        g.setColor(Color.BLUE);
        g.fillOval(x,y,r,r);
        g.setColor(Color.black);
        g.drawOval(x,y,r,r);
    }

    public Vector2D getPosition(){return position;}

    public void jump(){

    }

    public Brain getBrain() {
        return brain;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }
}
