package app;

import graph.Graph;
import graph.VertexException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class App
{
    private final Graph<String> graphCities;

    public App()
    {
        this.graphCities = new Graph<String>(0);
    }

    public App(BufferedReader b) throws IOException
    {
        this.graphCities = createGraph(b);
    }

    private Graph<String> createGraph(BufferedReader b) throws IOException
    {
        int order;
        ArrayList<String> cities;
        Graph<String> graphCities;

        order = Integer.parseInt(b.readLine());

        graphCities = new Graph<>(order);

        cities = new ArrayList<String>();

        for (int i = 0; i < order; i++) cities.add(b.readLine());

        for (int i = 0; i < order; i++)
        {
            String line = b.readLine();
            String[] edges = line.split(",");

            for (int j = 0; j < order; j++)
            {
                if (!Objects.equals(edges[j], "0")) graphCities.addEdge(cities.get(i), cities.get(j), Float.parseFloat(edges[j]));
                else graphCities.addEdge(cities.get(i), cities.get(j), 0);
            }
        }

        return graphCities;
    }

    public void addCity(String city)
    {
        this.graphCities.addVertex(city);
    }

    public void addRoute(String city1, String city2, float distance)
    {
        this.graphCities.addEdge(city1, city2, distance);
    }

    public void displayMST()
    {
        Graph<String> mst;

        try
        {
            mst = this.graphCities.minimumSpanningTree();
            System.out.println(mst);
        }
        catch (VertexException exception)
        {
            System.out.println("Impossível calcular AGM:");
            System.out.println(exception.getMessage());
        }
    }

    public void shortestPath(String city1, String city2)
    {
        System.out.println(this.graphCities.mapShortestPath(city1, city2));
    }

    public void shortestPathMST(String city1, String city2)
    {
        Graph<String> mst;

        try
        {
            mst = this.graphCities.minimumSpanningTree();
            System.out.println(mst.mapShortestPath(city1, city2));
        }
        catch (VertexException exception)
        {
            System.out.println("Impossível achar menor caminho entre vertices na AGM:");
            System.out.println(exception.getMessage());
        }
    }

    public void save(BufferedWriter b) throws IOException, NullPointerException
    {
        b.write(this.graphCities.toString());
    }

    public void saveMST(BufferedWriter b) throws IOException, NullPointerException
    {
        try
        {
            b.write(this.graphCities.minimumSpanningTree().toString());
        }
        catch (VertexException exception)
        {
            System.out.println("Impossível salvar AGM.");
            System.out.println(exception.getMessage());
        }
    }
}