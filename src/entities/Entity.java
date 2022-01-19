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
    private int stepsBeforeTurn = 20;
    private Color color = Color.BLUE;

    private double distance = 8;


    public Entity(Vector2D startPos)
    {
        this.position = new Vector2D(startPos.getX(), startPos.getY());
        this.brain = new Brain(this);
        this.direction = brain.getDirectionVector();
    }


    public void move(Map2D map){

        //Update Position and direction Vector
        double oldX = position.getX();
        double oldY = position.getY();
        if(stepsBeforeTurn <= 20){
            this.direction.add(brain.getDirectionVector().mult(0.1)).normalize();
            stepsBeforeTurn = 0;
        }

        this.position.add(this.direction);
        int blockType = map.getValueOf((int) this.position.getX() / Simulation.BLOCKSIZE, (int) this.position.getY() / Simulation.BLOCKSIZE);
        if(blockType == 1){
            this.position.sub(this.direction);
        }
        stepsBeforeTurn--;
    }

    public void draw(Graphics g){
        Vector2D pos = this.getPosition();
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        int r = 10;
        x = x-(r/2);
        y = y-(r/2);
        g.setColor(this.color);
        g.fillOval(x,y,r,r);
        g.setColor(Color.black);
        g.drawOval(x,y,r,r);
    }

    public void setColor(Color c){
        this.color = c;
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
