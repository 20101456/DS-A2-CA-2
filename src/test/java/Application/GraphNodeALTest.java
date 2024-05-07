package Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphNodeALTest {

    private GraphNodeAL<String> node1;
    private GraphNodeAL<String> node2;
    private GraphNodeAL<String> node3;

    @BeforeEach
    void setup() {
        node1 = new GraphNodeAL<>("Paris");
        node2 = new GraphNodeAL<>("London");
        node3 = new GraphNodeAL<>("Berlin");
    }

    @Test
    void getData() {
        assertEquals("Paris", node1.getData());
    }

    @Test
    void setData() {
        node1.setData("London");
        assertEquals("London", node1.getData());
    }

    @Test
    void getAdjList() {
        node1.connectToNodeDirected(node2);
        assertTrue(node1.getAdjList().contains(node2));
    }

    @Test
    void connectToNodeDirected() {
        node1.connectToNodeDirected(node2);
        assertTrue(node1.getAdjList().contains(node2));
        assertFalse(node2.getAdjList().contains(node1));
    }

    @Test
    void connectToNodeUndirected() {
        node1.connectToNodeUndirected(node2);
        assertTrue(node1.getAdjList().contains(node2));
        assertTrue(node2.getAdjList().contains(node1));
    }

    @Test
    void findPathDepthFirst() {
        node1.connectToNodeDirected(node2);
        List<GraphNodeAL<?>> path = GraphNodeAL.findPathDepthFirst(node1, new ArrayList<>(), "London");
        assertEquals(2, path.size());
        assertEquals(node1, path.get(0));
        assertEquals(node2, path.get(1));
    }

    @Test
    void findAllPathsDepthFirst() {
        node1.connectToNodeDirected(node2);
        node1.connectToNodeDirected(node3);
        node2.connectToNodeDirected(node3);
        List<List<GraphNodeAL<?>>> allPaths = GraphNodeAL.findAllPathsDepthFirst(node1, new ArrayList<>(), "Berlin");
        assertEquals(2, allPaths.size());
    }
}
