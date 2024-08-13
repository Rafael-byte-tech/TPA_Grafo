package graph;

class Path
{
    public float distance;
    public int previous;

    public Path(float weight, int source)
    {
        this.distance = weight;
        this.previous = source;
    }
}