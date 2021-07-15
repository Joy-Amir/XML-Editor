package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
            return;
        }

    }
    public void print_XML(Node node, ArrayList<Node> arr, int level,BufferedWriter dataWrite) {
        if (node == null)
            return;

        try {
            int comp = Integer.compare(node.getChildrenNum(), 0);
            String parent_value = node.getValue();
            String name = node.getName();
            int comp2 = Integer.compare(name.indexOf(' '), -1);
            int flag = 0;
            int flag_tagName = 0;
            int flag3 = 0;
            int m = 0;
            String s = "";
            String s2 = "";
            String s3 = "";



            //System.out.println(node.getChildrenNum());

            //comment handling
            if (node.getType().compareTo("comment") == 0 || node.getType().compareTo("preprocessor") == 0)
                return;

            if (node.getParent() != null) {
                for (m = 0; m < arr.size(); m++) {

                    s = node.getName();
                    s2 = "";
                    if (node.getValue().compareTo(arr.get(m).getValue()) == 0 && node.getName().compareTo(arr.get(m).getName()) == 0) {
                        flag3 = 1;
                        int j = s.indexOf(' ');
                        if (j != -1)
                            s = s.substring(0, j);
                        if (m != 0) {
                            s2 = arr.get(m - 1).getName();
                            int k = s2.indexOf(' ');
                            if (k != -1)
                                s2 = s2.substring(0, k);
                        }

                        if (m == 0 || s.compareTo(s2) != 0) {
                            dataWrite.write("\n");
                            for (int i = 0; i < (4 * level); i++)
                                dataWrite.write(" ");
                            dataWrite.write("\"" + s + "\": [");


                        }

                        if (m + 1 < arr.size()) {
                            s3 = arr.get(m + 1).getName();
                            int k = s3.indexOf(' ');
                            if (k != -1)
                                s3 = s3.substring(0, k);
                        }

                        break;

                    }

                }
                //<e name="value" id="val">text</e>

                if ((node.getType().compareTo("self-close")) != 0 && (name.indexOf(' ') != -1)) {
                    flag_tagName = 1;
                    //################ Modified
                    if (flag3 == 0) {
                        dataWrite.write("\n");
                        for (int i = 0; i < (4 * level); i++)
                            dataWrite.write(" ");
                        dataWrite.write("\"" + name.substring(0, name.indexOf(' ')) + "\": ");
                        dataWrite.write("{");
                    } else {
                        dataWrite.write("\n");
                        for (int i = 0; i < (4 * level) + 2; i++)
                            dataWrite.write(" ");
                        dataWrite.write("{");
                    }


                    int i = 0;
                    int j = 0;
                    int k;
                    boolean flag4 = false;

                    while ((i = name.indexOf(' ', j)) != -1) {
                        dataWrite.write("\n");
                        for (int o = 0; o < (4 * (level + 1)); o++)
                            dataWrite.write(" ");
                        //if(flag4)
                        flag4 = true;
                        dataWrite.write("\"@" + name.substring(i + 1, name.indexOf('=', i)) + "\": ");
                        dataWrite.write(name.substring(k = name.indexOf('"', i), (j = name.indexOf('"', k + 1))) + "\",");
                    }
                    if (!node.getValue().isEmpty()) {
                        dataWrite.write("\n");
                        for (int o = 0; o < (4 * (level + 1)); o++)
                            dataWrite.write(" ");
                        dataWrite.write("\"#text\": " + "\"" + node.getValue() + "\"");
                    }

                }//if

                //<e> text <a>text</a> </e>
                else if (comp != 0 && !parent_value.isEmpty()) {
                    flag_tagName = 1;
                    //###################Modified
                    if (flag3 == 0) {
                        dataWrite.write("\n");
                        for (int o = 0; o < (4 * (level)); o++)
                            dataWrite.write(" ");
                        dataWrite.write("\"" + node.getName() + "\": ");
                        dataWrite.write("{");
                    } else {
                        dataWrite.write("\n");
                        for (int i = 0; i < (4 * level) + 2; i++)
                            dataWrite.write(" ");
                        dataWrite.write("{");
                    }
                    dataWrite.write("\n");
                    for (int o = 0; o < (4 * (level + 1)); o++)
                        dataWrite.write(" ");
                    dataWrite.write("\"#text\": " + "\"" + node.getValue() + "\",");
                }


                //self close handling
                //#################Modified

                else if (node.getType().compareTo("self-close") == 0) {

                    if (name.indexOf(' ') != -1) {
                        if (flag3 == 0) {
                            dataWrite.write("\n");
                            for (int o = 0; o < (4 * (level)); o++)
                                dataWrite.write(" ");
                            dataWrite.write("\"" + name.substring(0, name.indexOf(' ')) + "\": ");
                            dataWrite.write("{");
                        } else {
                            dataWrite.write("\n");
                            for (int i = 0; i < (4 * level) + 2; i++)
                                dataWrite.write(" ");
                            dataWrite.write("{");
                        }

                        for (int i = 0; i < name.length(); i++) {
                            if (name.charAt(i) == ' ') {
                                dataWrite.write("\n");
                                for (int o = 0; o < (4 * (level + 1)); o++)
                                    dataWrite.write(" ");
                                dataWrite.write("\"@" + name.substring(name.indexOf(' ') + 1, name.indexOf('=')) + "\": ");
                                dataWrite.write(name.substring(name.indexOf('"'), name.indexOf('/')));

                                //return;
                            }


                        }//for

                    } else {
                        dataWrite.write("\n");
                        for (int o = 0; o < (4 * (level)); o++)
                            dataWrite.write(" ");
                        dataWrite.write("\"" + name.substring(0, name.indexOf('/')) + "\": null");

                    }
                }//if

                //<id>123</id>
                else if (comp==0){
                    flag = 1;
                    if (flag3 == 0) {
                        dataWrite.write("\n");
                        for (int o = 0; o < (4 * (level)); o++)
                            dataWrite.write(" ");
                        dataWrite.write("\"" + node.getName() + "\": ");
                        dataWrite.write("\"" + node.getValue() + "\"");
                    } else {
                        dataWrite.write("\n");
                        for (int i = 0; i < (4 * (level + 1)); i++) //#####################################################
                            dataWrite.write(" ");
                        dataWrite.write("\"" + node.getValue() + "\"");
                    }

                }
                //<e> <a>text</a> <b>text</b> </e>
                //<e> <a>text</a> <a>text</a> </e>
                if (comp != 0) {
                    int x;
                    if (parent_value.isEmpty()) {
                        if (flag_tagName == 0) {
                            //###########33Added
                            if (flag3 == 0) {
                                dataWrite.write("\n");
                                for (int o = 0; o < (4 * (level)); o++)
                                    dataWrite.write(" ");
                                dataWrite.write("\"" + node.getName() + "\": ");
                                dataWrite.write("{");
                            } else {
                                dataWrite.write("\n");
                                for (int i = 0; i < (4 * level) + 2; i++)
                                    dataWrite.write(" ");
                                dataWrite.write("{");
                            }

                        }
                    }
                    ArrayList<Node> son = node.getChildren();
                    arr = new ArrayList<Node>();

                    Collections.sort(son, Node.comp);
                    for (x = 0; x < node.getChildrenNum() - 1; x++) {
                        String S = son.get(x).getName();
                        String S2 = son.get(x + 1).getName();
                        int i = S.indexOf(' ');
                        int j = S2.indexOf(' ');

                        if (i != -1) {
                            S = S.substring(0, i);
                        }
                        if (j != -1) {
                            S2 = S2.substring(0, j);
                        }

                        if (S.compareTo(S2) == 0) {
                            arr.add(son.get(x));
                            arr.add(son.get(x + 1));
                        }
                    }

                }
            }

            ArrayList<Node> Child = node.getChildren();

            for (int i = 0; i < node.getChildrenNum(); i++) {

                if (i == 0 && node.getValue().compareTo("") != 0)
                    dataWrite.write(",");
                print_XML(Child.get(i), arr, level + 1,dataWrite);
                String Type = Child.get(i).getType();
                if (i < node.getChildrenNum() - 1 && Type.compareTo("comment") != 0 && Type.compareTo("preprocessor") != 0) {
                    dataWrite.write(","); //###################
                }
            }
            if (node.getParent() != null && flag == 0) {
                dataWrite.write("\n");
                for (int o = 0; o < (4 * (level) + 2); o++)
                    dataWrite.write(" ");
                dataWrite.write("}");

            }
            if (flag3 == 1 && (m == arr.size() - 1 || s.compareTo(s3) != 0)) {
                dataWrite.write("\n");
                for (int o = 0; o < (4 * (level)); o++)
                    dataWrite.write(" ");
                dataWrite.write("]");
            }
        }//try
        catch(Exception ex)
        {
            ex.printStackTrace();
            return;
        }

    }//fn


    public void print_xml(BufferedWriter dataWrite) {
        ArrayList<Node> array = new ArrayList<Node>();
        print_XML(root, array, -1,dataWrite);
        try{
        dataWrite.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return;
        }
        //System.out.print("HElloo");
    }

}





