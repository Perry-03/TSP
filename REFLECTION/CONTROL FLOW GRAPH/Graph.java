import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Graph {
    private final List<Edge> edges = new ArrayList<>();

    public void addEdge(final Edge e) { edges.add(e); }  
    
    public void printGraph() {
        for (Edge e : edges)
            System.out.println(e);        
    }

}
