package graph;

public class VertexException extends Exception
{
    private final String message;

    public VertexException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}