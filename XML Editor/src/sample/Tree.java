package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Tree
{
    private Node root;

    public long createTree(BufferedReader dataRead)
    {
        Integer index;
        long lines = 0;
        try
        {
            String s = "";
            String s2;
            Integer lastSearch = 0;
            Integer l;
            root = new Node("", "root", null);
            Node current = root;
            while ((s2 = dataRead.readLine() )!= null)
            {
                lines++;
                //Remove extra spaces if any
                s2 = s2.trim();
                s = s.concat(s2);
                l = s.length();
                while (l > 0)
                {
                    Integer compare = Character.compare(s.charAt(0),'<');

                    //If the data is a text "tag value"
                    if(compare !=0)
                    {
                        //get the index of the last character in the text
                        index = s.indexOf('<', lastSearch);

                        //if the index doesn't exist, then it is in the next line
                        if(index == -1)
                        {
                            //Update last search index to speed up the search in the next loop
                            lastSearch = l - 1;
                            s = s.concat(" ");
                            //break to get the rest of the text first
                            break;
                        }
                        //If the string length is less than the line limit, print it in a single line
                        //ADDED#####################################################

                        if(Character.compare(s.charAt(index - 1),' ') == 0)
                            current.setValue(s.substring(0,index-1));
                        else
                            current.setValue(s.substring(0,index));
                        s = s.substring(index);

                    }

                    //If the data is a tag
                    else
                    {
                        // Search for the end of the tag
                        index = s.indexOf(">", lastSearch);
                        if(index == -1)
                        {
                            lastSearch = l - 1;
                            s = s.concat(" ");
                            break;
                        }

                        //Check if it is open tag or closing tag
                        compare = Character.compare(s.charAt(1),'/');

                        //If it is a closing tag, decrement the indentation level
                        if(compare == 0)
                        {
                            current = current.getParent();

                        }
                        //Check if the tag is a comment, a processing directive or self closing tag
                        Integer compare2 = Character.compare(s.charAt(index - 1),'/');
                        Integer compare3 = Character.compare(s.charAt(1),'!');
                        Integer compare4 = Character.compare(s.charAt(1),'?');
                        //self closing tag
                        if(compare != 0)
                        {
                            if(compare2 == 0)
                            {
                                Node child = new Node(s.substring(1,index),"self-close",current);
                                current.getChildren().add(child);
                            }
                            else if(compare3 == 0)
                            {
                                Node child = new Node(s.substring(1,index),"comment",current);
                                current.getChildren().add(child);
                            }
                            else if(compare4 == 0)
                            {
                                Node child = new Node(s.substring(1,index),"preprocessor",current);
                                current.getChildren().add(child);
                            }
                            else
                            {
                                Node child = new Node(s.substring(1,index),"normal",current);
                                current.getChildren().add(child);
                                current = child;
                            }

                        }
                        s = s.substring(index + 1);


                    }


                    l = s.length();
                    lastSearch = 0;
                }
            }
            dataRead.close();

        }catch(Exception ex)
        {
            return -1;
        }
        return lines;
    }

    public void minify(BufferedWriter dataWrite)
    {
        minifyNode(root, dataWrite);
        try {
            dataWrite.close();
        }catch(Exception ex)
        {
            return;
        }

    }
    public void minifyNode(Node p, BufferedWriter dataWrite)
    {
        try {
            if (p == null || (p.getType().compareTo("comment")==0) ) {
                return;
            }
            if (p.getParent() != null) {
                dataWrite.write("<" + p.getName() + ">");
            }
            String type = p.getType();
            dataWrite.write( p.getValue());
            Integer childn = p.getChildrenNum();
            ArrayList<Node> Child = p.getChildren();
            for(Integer i = 0; i < childn; i++)
            {
                minifyNode(Child.get(i),dataWrite);
            }

            if (type.compareTo("normal")==0)
            {
                Integer last = p.getName().indexOf(" ");
                if (last == -1) {
                    dataWrite.write("</" + p.getName() + ">");
                }
                else
                {
                    dataWrite.write("</" + p.getName().substring(0, last) + ">");
                }
            }
        }catch(Exception ex)
        {
            return;
        }
    }
    void format(BufferedWriter dataWrite)
    {
        formatNode(root, dataWrite, -1, false);
        try {
            dataWrite.close();
        }catch(Exception ex)
        {
            return;
        }
    }
    void formatNode(Node p, BufferedWriter dataWrite, Integer level, boolean flag)
    {
        try {
            if (p == null) {
                return;
            }
            if (p.getParent() != null) {
                if(flag)
                {
                    dataWrite.write("\n");
                    for (Integer i = 0; i < (4 * level); i++)
                    {
                        dataWrite.write(" ");
                    }
                }
                flag = true;

                dataWrite.write("<" + p.getName() + ">");
                //System.out.println(("<" + p.getName() + ">"));
            }
            String type = p.getType();
            String value = p.getValue();
            Integer vlength = value.length();

            if(vlength < 100)
            {
                dataWrite.write(value);
                flag = false;
            }
            //else, print it in multiple lines
            else {
                Integer x = 0;
                while (vlength > 100) {
                    dataWrite.write("\n");
                    //same indentation level for the first line only
                    for (Integer i = 0; i < (4 * (level + x)); i++) {
                        dataWrite.write(" ");
                    }
                    Integer last = value.indexOf(" ", 80);
                    dataWrite.write(value.substring(0, last));
                    value = value.substring(last + 1);
                    vlength = vlength - (last + 1);
                    x = 1;
                }
                //Write the last line of length < 100
                dataWrite.write("\n");
                for (Integer i = 0; i < (4 * (level + 1)); i++) {
                    dataWrite.write(" ");
                }
                dataWrite.write(value.substring(0, vlength));
            }
            Integer childn = p.getChildrenNum();
            ArrayList<Node> Child = p.getChildren();
            Boolean flag2;
            for(Integer i = 0; i < childn; i++)
            {
                if(p.getParent()==null && i == 0)
                    flag2 = false;
                else
                    flag2 = true;
                formatNode(Child.get(i),dataWrite, level+1,flag2);
                flag = true;
            }

            if (type.compareTo("normal")== 0)
            {
                Integer last = p.getName().indexOf(" ");
                if(flag)
                {
                    dataWrite.write("\n");
                    for (Integer i = 0; i < (4 * level); i++)
                    {
                        dataWrite.write(" ");
                    }
                }
                if (last == -1) {
                    dataWrite.write("</" + p.getName() + ">");
                }
                else
                {
                    dataWrite.write("</" + p.getName().substring(0, last) + ">");
                }
            }
        }catch(Exception ex)
        {
            return;
        }

    }

}

