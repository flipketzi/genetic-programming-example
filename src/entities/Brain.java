package entities;

import math.Vector2D;

public class Brain {
    private TreeNode root;

    public Brain(Entity ent){
        root = new TreeNode(TreeNode.getRandomOperator(), ent, 2);
    }
    public Brain(TreeNode root){
        this.root = root;
    }

    public Vector2D getDirectionVector(){
        int rootCalc = root.calculate();
        int rootVal = Math.abs(rootCalc) % 360;
        double radiant = rootVal * (Math.PI / 180);
        Vector2D dir = new Vector2D(Math.cos(radiant), Math.sin(radiant));
        return dir;

    }

    public void mutate(double prob){
        root.mutate(prob);
    }

    public Brain copy(){
        return new Brain(this.root.copy());
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}
