package sample;

import java.util.ArrayList;

public class Node
{
    private String value = new String();
    private String name = new String();
    private String type = new String();
    ArrayList<Node> Child;
    Node Parent;
    public Node(){
        value = "";
        name = "";
        //ChildrenNum = 0;
        //Child = new Node [50];
        Child = new ArrayList<Node>(100);
    }
    public Node(String n, String t, Node p){
        name = n;
        value = "";
        type = t;
        Child = new ArrayList<Node>();
        Parent = p;
    }
    public void setName(String n){
        name = n;
    }
    public void setParent(Node p)
    {
        Parent = p;
    }
    public void setValue(String v){
        value = v;
    }
    public void setType(String t){
        type = t;
    }
    public void addChild(Node t){
        //Child[ChildrenNum] = t;
        Child.add(t);
        //ChildrenNum++;
    }
    public String getValue(){
        return value;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public Integer getChildrenNum(){
        //return ChildrenNum;
        return Child.size();
    }
    public ArrayList<Node> getChildren(){
        return Child;
    }
    public Node getParent(){
        return Parent;
    }

}

