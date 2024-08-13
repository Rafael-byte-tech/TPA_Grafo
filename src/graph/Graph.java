package graph;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Graph<T>
{
    private int order;
    private final ArrayList<T> vertices;
    private Float[][] edges;

    public Graph(int order)
    {
        this.order = order;
        this.vertices = new ArrayList<T>();
        this.edges = new Float[this.order][this.order];
    }

    private void enlargeAdjacencyMatrix()
    {
        // Create a new 2D array with the new dimensions
        Float[][] enlargedMatrix = new Float[this.order + 1][this.order + 1];

        // Copy elements from the original array to the new array
        for (int i = 0; i < this.edges.length; i++)
        {
            System.arraycopy(this.edges[i], 0, enlargedMatrix[i], 0, this.edges[i].length);
        }

        this.edges = enlargedMatrix;
        this.order = this.edges.length;
    }

    /**
     * Função adiciona vertex.
     * @param value, valor do vertex adicionado.*/
    public boolean addVertex(T value)
    {
        if (!this.vertices.contains(value))
        {
            if (!(this.vertices.size() < this.order)) this.enlargeAdjacencyMatrix();
            this.vertices.add(value);

            return true;
        }

        return false;
    }

    /**
     * Função adiciona aresta ao grafo. Adiciona mais vértices caso os vértices especificados não existam.
     * @param sourceValue, valor do vertex de origem.
     * @param destinyValue, valor do vertex de destino.
     * @param weight, peso da aresta.*/
    public void addEdge(T sourceValue, T destinyValue, float weight)
    {
        int sourceIndex, destinyIndex;

        sourceIndex = this.vertices.indexOf(sourceValue);
        destinyIndex = this.vertices.indexOf(destinyValue);

        if (this.addVertex(sourceValue)) sourceIndex = this.vertices.indexOf(sourceValue);
        if (this.addVertex(destinyValue)) destinyIndex = this.vertices.indexOf(destinyValue);

        if (sourceIndex > -1 && destinyIndex > -1 && weight >= 0)
        {
            this.edges[sourceIndex][destinyIndex] = weight;
            this.edges[destinyIndex][sourceIndex] = weight;
        }
    }

    /*-------------------------------MÉTODOS PARA ACHAR A ÁRVORE GERADORA MÍNIMA--------------------------------------*/

    /**
     * Gera a MST de um GRAFO CONEXO e PONDERADO.
     * @return mst, Árvore Geradora Mínima; null, caso encontre um vértice nulo.
     * */
    public Graph<T> minimumSpanningTree() throws VertexException
    {
        /*Variáveis*/
        Graph<T> mst;
        ArrayList<T> visitedVertices;
        PriorityQueue<Edge> edgePriorityQueue;
        Edge lesserEdge;
        T destinedVertex;
        int index;

        /*Inicialização de variáveis.*/
        mst = new Graph<T>(this.order);
        mst.vertices.addAll(this.vertices); /*Coloca vértices na lista de vértices da MST*/
        visitedVertices = new ArrayList<T>();
        edgePriorityQueue = new PriorityQueue<Edge>(new EdgeComparator());

        /*Se árvore tem vértices. Inicializa Lista de Vértices visitados.*/
        if (!this.vertices.isEmpty()) visitedVertices.addFirst(this.vertices.getFirst());
        else return mst;

        /*Inicializa fila de prioridades de arestas com arestas do Vértice 0.*/
        for (int destiny=0; destiny<this.order; destiny++)
        {
            if (this.edges[0][destiny] != null)
            {
                edgePriorityQueue.add(new Edge(0, destiny, this.edges[0][destiny]));
            }
        }

        /*Enquanto o total de vértices da MST for menor que a ordem do gráfo fonte*/
        while (visitedVertices.size() < this.vertices.size())
        {
            /*Pega aresta de menor peso da Fila de Prioridade.*/
            lesserEdge = edgePriorityQueue.poll();

            /*Pega o vértice de destino da aresta.*/
            if (lesserEdge != null)
            {
                index = lesserEdge.getDestinyIndex();
                destinedVertex = this.vertices.get(index);
            }
            /*Há Vertex desconexo.*/
            else throw new VertexException("Grafo está desconexo. Verifique se todos os vertexes estão conectados.");

            /*Verifica se o vértice está na lista de visitados.*/
            if (!visitedVertices.contains(destinedVertex))
            {
                /*Adiciona todas as arestas emitidas pelo vértice na fila de prioridade.*/
                for (int destiny=0; destiny<this.order; destiny++)
                {
                    if (this.edges[index][destiny] != null) edgePriorityQueue.add(new Edge(index, destiny, this.edges[index][destiny]));
                }

                /*Inclui vértice receptor na lista de visitados.*/
                visitedVertices.add(destinedVertex);

                /*Adiciona a aresta de menor peso na MST*/
                mst.edges[lesserEdge.getOriginIndex()][lesserEdge.getDestinyIndex()] = lesserEdge.getWeight();
                mst.edges[lesserEdge.getDestinyIndex()][lesserEdge.getOriginIndex()] = lesserEdge.getWeight();
            }
        }

        /*Retorna Árvore Geradora Mínima*/
        return mst;
    }

    /*-------------------------------MÉTODOS PARA ACHAR O MENOR CAMINHO ENTRE VÉRTICES--------------------------------*/

    /**
     * Função Auxiliar de getPaths(). Seleciona vértice mais próximo do vértice fonte.
     * @param paths, array com os caminhos do vértice fonte até cada um dos vértices do grafo.
     * @param visitedVertices, lista de indexes dos vértices vizitados.
     * @return int nearest, INDEX do vértice não vizitado mais próximo do vértice fonte.*/
    private int getNearest(Path[] paths, ArrayList<Integer> visitedVertices)
    {
        int nearest;

        nearest = 0;

        for (int i=0; i<this.vertices.size(); i++)
        {
            if (!visitedVertices.contains(i))
            {
                if (paths[i].distance < paths[nearest].distance || visitedVertices.contains(nearest))
                {
                    nearest = i;
                }
            }
        }

        return nearest;
    }

    /**
     * Função recebe vértice qualquer do grafo e retorna um Array contendo os menores caminhos de cada um dos
     * dos outros vértices do grafo até o vértice fonte. Usa o "Algoritmo de Djikstra".
     * @param source, vértice fonte a partir do qual se calcula os menores caminhos até cada um dos vértices.
     * @return paths, Array contendo os menores caminhos possíveis de source até os outros vértices.*/
    private Path[] getPaths(T source) throws VertexException
    {
        Path[] paths;                       /*Array de menores caminhos possíveis*/
        ArrayList<Integer> visitedVertices; /*Lista de vértices visitados*/

        /*Verifica se source existe no grafo.*/
        if (!this.vertices.contains(source)) throw new VertexException(source.toString() + " não existe no grafo.");

        paths = new Path[this.vertices.size()];
        visitedVertices = new ArrayList<Integer>();

        /*Atribui valor "infinito" à distância e -1 ao INDEX do vértice prévio.*/
        for (int i=0; i<this.vertices.size(); i++)
        {
            paths[i] = new Path(Float.MAX_VALUE, -1);
        }

        /*Atribui valor 0 à distância do vértice source até ele mesmo.*/
        paths[this.vertices.indexOf(source)].distance = 0;

        /*Constroi caminho enquanto todos os vértices não forem vizitados.*/
        while (visitedVertices.size() < this.vertices.size())
        {
            /*Seleciona vertex com MENOR DISTÂNCIA da FONTE como vertex corrente.*/
            int current = getNearest(paths, visitedVertices);

            /*Itera cada vértice do grafo*/
            for (int i=0; i<this.vertices.size(); i++)
            {
                /*Verifica na matriz de adjacência se o vértice é vizinho do VÉRTICE CORRENTE.*/
                if (this.edges[current][i] != null)
                {
                    /*DISTÂNCIA(src, vizinho) = DISTÂNCIA(src, corrente) + DISTÂNCIA(corrente, vizinho).*/
                    float distance = paths[current].distance + this.edges[current][i];

                    /*Se nova distância calculada é menor que a distância atual.*/
                    if (distance < paths[i].distance)
                    {
                        paths[i].distance = distance; /*Atualiza DISTÂNCIA(src, vizinho).*/
                        paths[i].previous = current;  /*Atualiza vértice prévio.*/
                    }
                }
            }

            visitedVertices.add(current);   /*Lista de visitados adiciona vértice corrente.*/
        }

        return paths;   /*Retorna menores caminhos possíveis.*/
    }

    /**
     * Função informa menor caminho entre dois vértices na forma de lista.
     * @param paths, lista de todos os menores caminhos entre um vértice fonte e todos os vértices do grafo.
     * @param source, vértice fonte a partir do qual são calculados os menores caminhos.
     * @param destiny, vértice destino do caminho.
     * @return shortestPath, lista dos vértices por onde o caminho passa. É ordenada de FONTE -> DESTINO.*/
    private ArrayList<T> getShortestPath(Path[] paths, T source, T destiny) throws VertexException, IndexOutOfBoundsException
    {
        int index;
        ArrayList<T> shortestPath;

        if (!this.vertices.contains(source) || !this.vertices.contains(destiny)) throw new VertexException("Vértice não existe no grafo.");

        index = this.vertices.indexOf(destiny);
        shortestPath = new ArrayList<T>();

        shortestPath.add(this.vertices.get(index));

        while (index != this.vertices.indexOf(source))
        {
            index = paths[index].previous;                /*Source is -1*/

            if (index < 0 || index >= this.vertices.size()) throw new IndexOutOfBoundsException();

            shortestPath.add(this.vertices.get(index)); /*-1 is out of bound*/
        }

        return shortestPath;
    }

    /**
     * Função retorna menor distância possível para dado destino.
     * @param paths, Array de menores caminhos possíveis entre um dado vértice fonte e todos os vértices do grafo.
     * @param destiny, vértice destino, do qual se quer saber a distância do vértice fonte.
     * @return distância total do vértice fonte até o destino.*/
    private float getShortestDistance(Path[] paths, T destiny)
    {
        return paths[this.vertices.indexOf(destiny)].distance;
    }

    /*------------------------------------------------------------------------------------------------------------*/

    /**Função retorna string contendo menor caminho e distância entre um vértice fonte e um vértice destino.
     * @param source, Vértice fonte de onde o caminho parte.
     * @param destiny, vértice destino onde o caminho termina.
     * @return string, se os dois vértices dados como parametros são válidos e existe um caminho entre eles,
     * retorna string contendo os vértices pelos quais o caminho passaa e a distância total entre fonte e destino.
     * Se não, retorna uma menssagem informando o erro.*/
    public String mapShortestPath(T source, T destiny)
    {
        StringBuilder string;       /*String com o resultado*/
        Path[] paths;               /*Todos os menores caminhos a partir do vértice source*/
        ArrayList<T> shortestPath;  /*Lista para guardar o caminho entre source e destiny*/
        float shortestDistance;     /*Distância do caminho entre source e destiny*/

        try
        {
            string = new StringBuilder();
            paths = getPaths(source);
            shortestPath = getShortestPath(paths, source, destiny);
            shortestDistance = getShortestDistance(paths, destiny);

            /*Reverte a lista para ficar na ordem destiny->source.*/
            Collections.reverse(shortestPath);

            /*Adiciona cada vértice do caminho na string.*/
            for (T vertex : shortestPath)
            {
                string.append(vertex.toString());
                string.append("->");
            }

            String s = "\b\b\nTotal: " + shortestDistance;
            string.append(s);
        }

        /*source ou destiny não estão no grafo*/
        catch (VertexException exception)
        {
            return exception.getMessage();
        }

        /*Não há caminho entre source e destiny.*/
        catch (IndexOutOfBoundsException exception)
        {
            return "Não há caminho " + "(" + source.toString() + ", " + destiny.toString() + ")";
        }

        return string.toString();
    }

    /**
     * Função gera string contendo info do grafo.
     * @return string contendo ordem, vértices e matriz de adjacência do grafo.*/
    @Override
    public String toString()
    {
        StringBuilder string;
        DecimalFormat df;

        string = new StringBuilder();
        df = new DecimalFormat("0.#");

        string.append(this.order);
        string.append("\n");

        for (T vertex : this.vertices)
        {
            string.append(vertex);
            string.append("\n");
        }

        for (int i=0; i<this.order; i++)
        {
            for (int j=0; j<this.order; j++)
            {
                if (this.edges[i][j] != null) string.append(df.format(this.edges[i][j]));
                else string.append(0);
                string.append(",");
            }
            string.deleteCharAt(string.length() - 1);
            string.append("\n");
        }

        return string.toString();
    }
}