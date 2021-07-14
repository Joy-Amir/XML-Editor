package sample;

public class HTElement {
    private Integer code;
    private String text;



    public HTElement(Integer c, String t)
    {
        code = c;
        text = t;
    }

    public void setCode(Integer c)
    {
        code = c;
    }
    public Integer getCode ()
    {
        return code;
    }
    public String getText ()
    {
        return text;
    }
    public void setText(String t)
    {
        text = t;
    }

}


