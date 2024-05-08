package Application;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> {
    private T data;
    private List<GraphNodeAL<T>> adjList;

    public GraphNodeAL(T data) {
        this.data = data;
        this.adjList = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<GraphNodeAL<T>> getAdjList() {
        return adjList;
    }

    public void connectToNodeDirected(GraphNodeAL<T> destNode) {
        adjList.add(destNode);
    }

    public void connectToNodeUndirected(GraphNodeAL<T> destNode) {
        adjList.add(destNode);
        destNode.getAdjList().add(this);
    }

    public static <T> List<GraphNodeAL<?>> findPathDepthFirst(GraphNodeAL<?> from, List<GraphNodeAL<?>> encountered, T lookingfor){
        List<GraphNodeAL<?>> result;
        if(from.data.equals(lookingfor)) { //Found it
            result=new ArrayList<>(); //Create new list to store the path info (any List implementation could be used)
            result.add(from); //Add the current node as the only/last entry in the path list
            return result; //Return the path list
        }
        if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
        encountered.add(from);
        for(GraphNodeAL<?> adjNode : from.adjList)
            if(!encountered.contains(adjNode)) {
                result=findPathDepthFirst(adjNode,encountered,lookingfor);
                if(result!=null) { //Result of the last recursive call contains a path to the solution node
                    result.add(0,from); //Add the current node to the front of the path list
                    return result; //Return the path list
                }
            }
        return null;
    }

    //Recursive depth-first search of graph (all paths identified returned)
    public static <T> List<List<GraphNodeAL<?>>> findAllPathsDepthFirst(GraphNodeAL<?> from, List<GraphNodeAL<?>> encountered, T lookingfor){
        List<List<GraphNodeAL<?>>> result=null, temp2;
        if(from.data.equals(lookingfor)) { //Found it
            List<GraphNodeAL<?>> temp=new ArrayList<>(); //Create new single solution path list
            temp.add(from); //Add current node to the new single path list
            result=new ArrayList<>(); //Create new "list of lists" to store path permutations
            result.add(temp); //Add the new single path list to the path permutations list
            return result; //Return the path permutations list
        }
        if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
        encountered.add(from); //Add current node to encountered list
        for(GraphNodeAL<?> adjNode : from.adjList){
            if(!encountered.contains(adjNode)) {
                temp2=findAllPathsDepthFirst(adjNode,new ArrayList<>(encountered),lookingfor); //Use clone of encountered list
                // /for recursive call!
                if(temp2!=null) { //Result of the recursive call contains one or more paths to the solution node
                    for(List<GraphNodeAL<?>> x : temp2) //For each partial path list returned
                        x.add(0,from); //Add the current node to the front of each path list
                    if(result==null) result=temp2; //If this is the first set of solution paths found use it as the result
                    else result.addAll(temp2); //Otherwise append them to the previously found paths
                }
            }
        }
        return result;
    }
}