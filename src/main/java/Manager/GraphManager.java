package Manager;

import Application.GraphNodeAL;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GraphManager {
    public Map<Point, GraphNodeAL<Point>> nodes = new HashMap<>();

    public void constructGraphFromImage(Image image) {
        PixelReader reader = image.getPixelReader();

        // First pass to create nodes
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = reader.getColor(x, y);
                if (color.equals(Color.WHITE)) {
                    Point point = new Point(x, y);
                    nodes.put(point, new GraphNodeAL<>(point));
                }
            }
        }

        // Second pass to create edges
        for (GraphNodeAL<Point> node : nodes.values()) {
            Point point = node.getData();
            connectToNeighbors(point, node);
        }
    }

    private void connectToNeighbors(Point point, GraphNodeAL<Point> node) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // Left, right, up, down

        for (int[] direction : directions) {
            Point neighborPoint = new Point(point.x + direction[0], point.y + direction[1]);
            GraphNodeAL<Point> neighborNode = nodes.get(neighborPoint);

            if (neighborNode != null) {
                node.connectToNodeUndirected(neighborNode);
            }
        }
    }

    // Method to add a new node to the graph
    public void addNode(GraphNodeAL<Point> node) {
        Point point = node.getData();
        nodes.put(point, node);
    }
}
