package sample;

public class TSTNode {
    private char data;
    private int code;
    private boolean end;
    private TSTNode left;
    private TSTNode mid;
    private TSTNode right;

    public TSTNode(char d)
    {
        data = d;
        left = null;
        mid = null;
        right = null;
        end = false;
    }

    public void setCode(int c)
    {
        code = c;
    }
    public int getCode ()
    {
        return code;
    }
    public char getData ()
    {
        return data;
    }
    public void setEnd(boolean e)
    {
        end = e;
    }
    public boolean getEnd ()
    {
        return end;
    }
    public void setLeft(TSTNode l)
    {
        left = l;
    }
    public TSTNode getLeft ()
    {
        return left;
    }
    public void setRight(TSTNode r)
    {
        right = r;
    }
    public TSTNode getRight ()
    {
        return right;
    }
    public void setMid(TSTNode m)
    {
        mid = m;
    }
    public TSTNode getMid ()
    {
        return mid;
    }
}

