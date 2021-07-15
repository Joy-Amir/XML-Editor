package sample;

public class TST {
    private TSTNode TSTRoot;
    public TST(TSTNode r)
    {
        TSTRoot = r;
        TSTRoot.setEnd(true);
    }
    public TST()
    {
        TSTRoot = null;
    }
    public TSTNode getTSTRoot()
    {
        return TSTRoot;
    }

    //The function returns the length of the longest string that can be decoded
    //The parameters: p is the tree node to be tested, result is an array of a single TSTNode "An array is to be passed
    // by reference as in Java, only arrays are passed by reference", it returns the last node containing matching character
    //The result node will be used on invoking the insert function, and in getting the code using result[0].getCode() function
    //"word" is the string to be encoded
    //"index" is initialized by zero, it represents the index of the character in the "word" that we are now searching for a match
    //"lastmatch" is used to trace the last matching index that will be returned at the end "it is initialized by zero"
    Integer search(TSTNode p, TSTNode[] result, String word, Integer index, Integer lastmatch)
    {
        if(p == null)
            return lastmatch;
        //Get the character of the node
        char c = p.getData();
        //Compare the character against the current word index
        Integer compare = Character.compare(word.charAt(index),c );

        //If it is less than character c, search in left node
        if(compare < 0)
        {
            return search(p.getLeft(), result, word, index, lastmatch);
        }
        //If it is less than character c, search in right node
        else if(compare > 0)
        {
            return search(p.getRight(),result, word, index, lastmatch);
        }
        //If equal, check if the word has finished
        else
        {
            index ++;
            //If the word hasn't finished, search for the next character in the word starting from the mid child
            if (index < word.length()) {
                if(p.getEnd() == true) {
                    result[0] = p;
                    lastmatch = index;
                }
                return search(p.getMid(),result, word, index, lastmatch);
            }

            else
            {
                if(p.getEnd() == true)
                {
                    result[0] = p;
                    return index;
                }
                else {

                    return lastmatch;
                }
                //return length of encoded string

            }
        }
    }
    //p is the node after which a new character is to be inserted
    // c is the character to be inserted "Note that each time we add a new character to an already existing string"
    //"first" initialized to true
    // code is the code of the new string
    void insert(TSTNode p, char c, Boolean first, int code)
    {
        if(first)
        {
            if(p.getMid() == null)
            {TSTNode n = new TSTNode(c);
                n.setCode(code);
                n.setEnd(true);
                p.setMid(n);
                return;
            }
            else if(Character.compare(p.getMid().getData(),c)==0)
            {
                p.getMid().setEnd(true);
            }
            else
            {
                insert(p.getMid(), c, false, code);
            }
        }
        else
        {
            char d = p.getData();
            Integer compare = Character.compare(c,d );
            if(compare < 0)
            {
                if(p.getLeft()==null)
                {
                    TSTNode n = new TSTNode(c);
                    n.setCode(code);
                    n.setEnd(true);
                    p.setLeft(n);
                }
                else
                    insert(p.getLeft(), c, false, code);
            }
            else if(compare > 0)
            {
                if(p.getRight()==null)
                {
                    TSTNode n = new TSTNode(c);
                    n.setCode(code);
                    n.setEnd(true);
                    p.setRight(n);
                }
            }
            else
            {
                p.setEnd(true);
            }

        }
    }
}

