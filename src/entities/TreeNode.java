package entities;

import com.sun.source.tree.Tree;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class TreeNode {
    public final static String[] TYPES = new String[]{"operator", "variable", "value"};
    public final static String[] CONSTANTS = new String[]{"variable", "value"};
    public final static String[] VARIABLES = new String[]{"x", "y"};
    public final static String[] OPERATORS = new String[]{"+", "-", "*"};
    public final static String[] VALUES = new String[]{"1", "2", "3", "4", "5"};

    public static String getRandomOperator(){
        return OPERATORS[new Random().nextInt(OPERATORS.length)];
    }

    public static String getRandomConstant(){
        String type = CONSTANTS[new Random().nextInt(CONSTANTS.length)];
        return switch (type) {
            case "variable" -> VARIABLES[new Random().nextInt(VARIABLES.length)];
            case "value" -> VALUES[new Random().nextInt(VALUES.length)];
            default -> "";
        };
    }


    private String type;
    private Entity ent;
    private ArrayList<TreeNode> children = new ArrayList<>();

    //Value Node
    public TreeNode(String type, Entity ent, int depth){
        this.type = type;
        this.ent = ent;

        switch (type) {
            case "+" -> this.growChildren(depth);
            case "-" -> this.growChildren(depth);
            case "*" -> this.growChildren(depth);
        }
    }

    public int calculate(){
        switch(type){
            //Operators
            case "+": return children.get(0).calculate() + children.get(1).calculate();
            case "-": return children.get(0).calculate() - children.get(1).calculate();
            case "*": return children.get(0).calculate() * children.get(1).calculate();
            case "/": return (int) children.get(0).calculate() / children.get(1).calculate();
            //Vars
            case "x": return (int) ent.getPosition().getX();
            case "y": return (int) ent.getPosition().getY();
        }
        //Value
        return Integer.valueOf(type);
    }

    public void setChildren(TreeNode node1, TreeNode node2){
        this.children = new ArrayList<>();
        this.children.add(node1);
        this.children.add(node2);
    }

    public void growChildren(int remainingDepth){
        if(remainingDepth <= 1){
            children.add(new TreeNode(this.getRandomConstant(), this.ent, (remainingDepth - 1)));
            children.add(new TreeNode(this.getRandomConstant(), this.ent, (remainingDepth - 1)));
        }else{
            children.add(new TreeNode(this.getRandomOperator(), this.ent, (remainingDepth - 1)));
            children.add(new TreeNode(this.getRandomOperator(), this.ent, (remainingDepth - 1)));
        }

    }

    public void printTree(){
        System.out.print("{");
        if(children.size() != 0){
            this.children.get(0).printTree();
            System.out.println(this.type);
            this.children.get(1).printTree();
        }else{
            System.out.print(this.type);
        }
        System.out.print("}");
    }
}
