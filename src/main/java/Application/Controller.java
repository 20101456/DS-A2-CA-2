package Application;

import Manager.GraphManager;
import Manager.LandmarkManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;

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
    ListView<Landmark> listView;
    private GraphManager graphManager = new GraphManager();  // The GraphManager for managing the graph
    private LandmarkManager landmarkManager = new LandmarkManager(graphManager);

    // The Exit method to exit the application
    @FXML
    public void Exit() {
        Platform.exit();
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

                listView.getItems().add(landmark);

                // Create a new Circle
                Circle circle = new Circle(event.getX(), event.getY(), 5);  // Adjust the radius as needed

                // Customize the Circle (optional)
                circle.setFill(Color.RED);  // Change the color of the circle

                // Add the Circle to the Pane that contains your ImageView
                pane.getChildren().add(circle);
            }
        }
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
}