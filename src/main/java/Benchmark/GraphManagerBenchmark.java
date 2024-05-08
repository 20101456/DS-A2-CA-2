package Benchmark;

import Manager.GraphManager;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class GraphManagerBenchmark {

    private GraphManager graphManager;
    private Image image;

    @Setup(Level.Trial)
    public void setup() {
        graphManager = new GraphManager();
        image = createTestImage(100, 100);  // Create a test image of size 100x100
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testConstructGraphFromImage() {
        graphManager.constructGraphFromImage(image);
    }

    // Helper method to create a test image
    private Image createTestImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, Color.WHITE);
            }
        }

        return image;
    }
}
