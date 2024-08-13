package graph;

public class Edge
{
    private int originIndex, destinyIndex;
    private Float weight;

    public Edge(int originIndex, int destinyIndex, float weight)
    {
        this.originIndex = originIndex;
        this.destinyIndex = destinyIndex;
        this.weight = weight;
    }

    public int getOriginIndex() {
        return originIndex;
    }

    public int getDestinyIndex()
    {
        return destinyIndex;
    }

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public void setOriginIndex(int originIndex)
    {
        this.originIndex = originIndex;
    }

    public void setDestinyIndex(int destinyIndex)
    {
        this.destinyIndex = destinyIndex;
    }
}
