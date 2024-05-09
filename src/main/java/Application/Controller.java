package Application;

import Manager.GraphManager;
import Manager.LandmarkManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Controller {
    @FXML
    public Pane pane;
    @FXML
    public Pane pane1;
    @FXML
    private ImageView imageView;  // The ImageView to display the original image
    @FXML
    private ImageView bwImageView;
    @FXML
    private TextField landmarkNameField;  // The TextField for the landmark name
    @FXML
    private TextField streetNameField;  // The TextField for the street name
    @FXML
    private TextField culturalValueField;  // The TextField for the cultural value
    @FXML
    private TextField landmark1TextField;
    @FXML
    private TextField landmark2TextField;
    @FXML
    private TextField limitTextField;
    @FXML
    ListView<Landmark> listView;
    private GraphManager graphManager = new GraphManager();  // The GraphManager for managing the graph
    private LandmarkManager landmarkManager = new LandmarkManager(graphManager);
    private List<Landmark> landmarks = new ArrayList<>();
    private Map<Pair<Landmark, Landmark>, Line> lines = new HashMap<>();
    private List<Color> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE);
    private List<Line> lines1 = new ArrayList<>();

    // The Exit method to exit the application
    @FXML
    public void Exit() {
        Platform.exit();
    }

    @FXML
    public void onPrintButtonClicked() {
        landmarkManager.printAllConnections();
    }

    //----------------
    // File Opener
    //----------------

    // The openImage method to open an image file
    @FXML
    public void openImage() {
        File file = getFileFromChooser();
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setOnMouseClicked(this::addLandmark);

            // Clear any existing landmarks
            landmarkManager.clear();
            pane.getChildren().removeIf(node -> node instanceof Circle);

            // Load the landmarks from the CSV file
            loadLandmarks("src/main/resources/landmarks.csv");
        }
    }

    // The getFileFromChooser method to get a file from the FileChooser
    private File getFileFromChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    //----------------
    // Landmarks
    //----------------
    private boolean isAddingLandmark = false;
    private Landmark lastAddedLandmark = null;

    public void toggleAddingLandmark() {
        isAddingLandmark = !isAddingLandmark;
    }

    public void addLandmark(MouseEvent event) {
        if (isAddingLandmark) {
            // Check if the event is within the bounds of imageView
            if (event.getX() < imageView.getFitWidth() && event.getY() < imageView.getFitHeight()) {
                String name = landmarkNameField.getText();
                String street = streetNameField.getText();
                String culturalValue = culturalValueField.getText();

                // Create and add the landmark
                Landmark landmark = landmarkManager.addLandmark(event, imageView, name, street, culturalValue);

                // Add the landmark to the list
                landmarks.add(landmark);

                // Add an edge in the graph between the newly added landmark and the previously added landmark
                if (lastAddedLandmark != null) {
                    landmarkManager.addEdge(lastAddedLandmark, landmark);
                }
                lastAddedLandmark = landmark;
                listView.getItems().add(landmark);

                drawLandmark(event);
            }
        }
    }

    public void drawLandmark(MouseEvent event){
        // Create a new Circle
        Circle circle = new Circle(event.getX(), event.getY(), 5);  // Adjust the radius as needed

        // Customize the Circle (optional)
        circle.setFill(Color.RED);  // Change the color of the circle

        // Add the Circle to the Pane that contains your ImageView
        pane.getChildren().add(circle);
    }

    public void connectLandmarks() {
        String landmark1Name = landmark1TextField.getText();
        String landmark2Name = landmark2TextField.getText();

        Landmark landmark1 = getLandmarkByName(landmark1Name);
        Landmark landmark2 = getLandmarkByName(landmark2Name);

        if (landmark1 != null && landmark2 != null) {
            // Connect the landmarks
            landmarkManager.addEdge(landmark1, landmark2);
        }
    }

    private Landmark getLandmarkByName(String name) {
        for (Landmark landmark : landmarks) {
            if (landmark.getName().equals(name)) {
                return landmark;
            }
        }
        return null; // return null if no landmark with the given name is found
    }

    //----------------
    // Paths
    //----------------
    private Pair<GraphNodeAL<Landmark>, GraphNodeAL<Landmark>> getPathNodes(String landmark1Name, String landmark2Name) {
        Landmark landmark1 = getLandmarkByName(landmark1Name);
        Landmark landmark2 = getLandmarkByName(landmark2Name);

        if (landmark1 == null || landmark2 == null) {
            System.out.println("One or both landmarks not found");
            return null;
        }

        // Get the nodes corresponding to the landmarks
        GraphNodeAL<Landmark> node1 = landmarkManager.getNodeByLandmark(landmark1);
        GraphNodeAL<Landmark> node2 = landmarkManager.getNodeByLandmark(landmark2);

        if (node1 == null || node2 == null) {
            System.out.println("One or both nodes not found");
            return null;
        }

        return new Pair<>(node1, node2);
    }

    public void highlightPath() {
        Pair<GraphNodeAL<Landmark>, GraphNodeAL<Landmark>> nodes = getPathNodes(landmark1TextField.getText(), landmark2TextField.getText());

        if (nodes != null) {
            // Find a path between the nodes
            List<GraphNodeAL<?>> path = GraphNodeAL.findPathDepthFirst(nodes.getKey(), null, nodes.getValue().getData());

            // Choose a single color for the path
            Color color = Color.RED;  // Change this to whatever color you want

            // Highlight the path
            for (int i = 0; i < path.size() - 1; i++) {
                Landmark start = (Landmark) path.get(i).getData();
                Landmark end = (Landmark) path.get(i + 1).getData();
                drawEdge(start, end, color);  // Use the chosen color
            }
        }
    }

    public void highlightPaths() {
        Pair<GraphNodeAL<Landmark>, GraphNodeAL<Landmark>> nodes = getPathNodes(landmark1TextField.getText(), landmark2TextField.getText());

        if (nodes != null) {
            // Find all path permutations between the nodes
            List<List<GraphNodeAL<?>>> allPaths = GraphNodeAL.findAllPathsDepthFirst(nodes.getKey(), null, nodes.getValue().getData());

            // Get the limit from the TextField
            String limitText = limitTextField.getText();
            int limit = limitText.isEmpty() ? allPaths.size() : Integer.parseInt(limitText);  // If no limit is provided, set it to the number of all paths

            // Limit the number of paths to highlight
            int pathsToHighlight = Math.min(allPaths.size(), limit);

            // Highlight the paths
            for (int i = 0; i < pathsToHighlight; i++) {
                List<GraphNodeAL<?>> path = allPaths.get(i);
                Color color = colors.get(i % colors.size());  // Get a color from the list
                for (int j = 0; j < path.size() - 1; j++) {
                    Landmark start = (Landmark) path.get(j).getData();
                    Landmark end = (Landmark) path.get(j + 1).getData();
                    drawEdge(start, end, color);  // Draw the edge with the chosen color
                }
            }
        }
    }

    //----------------
    // Other
    //----------------
    public void revert() {
        for (Line line : lines1) {
            pane.getChildren().remove(line);  // Remove the line from the ImageView
        }
        lines1.clear();  // Clear the list of lines
    }

    public void drawEdge(Landmark landmark1, Landmark landmark2, Color color) {
        // Calculate the scale factors
        double scaleX = imageView.getFitWidth() / imageView.getImage().getWidth();
        double scaleY = imageView.getFitHeight() / imageView.getImage().getHeight();

        // Calculate the layout offsets
        double offsetX = imageView.getLayoutX();
        double offsetY = imageView.getLayoutY();

        // Create a new Line object
        Line line = new Line();

        // Set the start and end points of the line to the locations of the landmarks,
        // scaled to the size of the ImageView and offset by the layout coordinates
        line.setStartX(landmark1.getX() * scaleX - offsetX);
        line.setStartY(landmark1.getY() * scaleY - offsetY);
        line.setEndX(landmark2.getX() * scaleX - offsetX);
        line.setEndY(landmark2.getY() * scaleY - offsetY);

        // Customize the line (optional)
        line.setStroke(color);  // Change the color of the line
        line.setStrokeWidth(2);  // Change the width of the line

        // Add the line to the Pane that contains your ImageView
        pane.getChildren().add(line);
        lines.put(new Pair<>(landmark1, landmark2), line);
        lines1.add(line);
    }


    @FXML
    public void processAndDisplayImage() {
        // Get the original image from the ImageView
        Image input = imageView.getImage();

        // Process the image
        Image output = ImageProcessor.processImage(input);

        // Set the processed image in the existing ImageView
        bwImageView.setImage(output);
    }

    @FXML
    public void constructGraphFromImage() {
        // Get the processed image from the ImageView
        Image output = bwImageView.getImage();

        // Convert the processed image to a graph
        graphManager.constructGraphFromImage(output);
    }

    public void loadLandmarks(String filename) {
        try (Reader in = new FileReader(filename)) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader("Name", "Street", "X", "Y", "CulturalValue")
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = format.parse(in);
            for (CSVRecord record : records) {
                String name = record.get("Name");
                String street = record.get("Street");
                int x = Integer.parseInt(record.get("X"));
                int y = Integer.parseInt(record.get("Y"));
                String culturalValue = record.get("CulturalValue");

                double scaleX = imageView.getFitWidth() / imageView.getImage().getWidth();
                double scaleY = imageView.getFitHeight() / imageView.getImage().getHeight();

                // Apply the scale factors to the x and y coordinates
                double scaledX = x * scaleX;
                double scaledY = y * scaleY;

                // Create a new MouseEvent with the scaled coordinates
                MouseEvent event = new MouseEvent(MouseEvent.MOUSE_CLICKED, scaledX, scaledY, scaledX, scaledY, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null);

                // Set the text fields with the landmark's data
                landmarkNameField.setText(name);
                streetNameField.setText(street);
                culturalValueField.setText(culturalValue);

                // Call the addLandmark method with the MouseEvent
                addLandmark(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}