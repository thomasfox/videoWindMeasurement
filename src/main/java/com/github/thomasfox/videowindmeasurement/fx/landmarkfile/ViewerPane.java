package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import static com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil.getColor;

import java.io.File;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;
import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;
import com.github.thomasfox.videowindmeasurement.xml.Box;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ViewerPane extends BorderPane
{
  Canvas canvasLayer = new Canvas();

  private ImageView imageView = new ImageView();

  private final BackButton backButton;
  
  private final LandmarksIndex landmarksIndexLabel;
  
  private final ForwardButton forwardButton;
  
  private final HBox controlBar = new HBox();
  
  private List<Landmarks> landmarksList;
  
  private File landmarksDirectory;
  
  private Scene scene;
  
  private int landmarksIndex = 0;
  
  public ViewerPane(Scene scene)
  {
    this.scene = scene;
    setStyle("-fx-background-color: #c8c8c8;");

    controlBar.setAlignment(Pos.CENTER);
    controlBar.setPadding(new Insets(5, 10, 5, 10));
    BorderPane.setAlignment(controlBar, Pos.CENTER);

    Pane imagePane = new Pane(imageView, canvasLayer);
    setCenter(imagePane);
 
    backButton = new BackButton(this);
    controlBar.getChildren().add(backButton);
    
    Label spacer = new Label("     ");
    controlBar.getChildren().add(spacer);

    landmarksIndexLabel = new LandmarksIndex();
    controlBar.getChildren().add(landmarksIndexLabel);
    
    spacer = new Label("     ");
    controlBar.getChildren().add(spacer);

    forwardButton = new ForwardButton(this);
    controlBar.getChildren().add(forwardButton);

    setBottom(controlBar);
  }
  
  public void setLandmarksList(List<Landmarks> landmarksList, File landmarksDirectory)
  {
    this.landmarksList = landmarksList;
    this.landmarksDirectory = landmarksDirectory;
    setLandmarksIndex(0);
    landmarksIndexLabel.setMaxIndex(landmarksList.size());
  }
  
  public List<Landmarks> getLandmarksList()
  {
    return landmarksList;
  }
  
  public void show(Landmarks landmarks)
  {
    Image image = landmarks.getImage();
    double maxWidth = scene.getWidth();
    double maxHeight = scene.getHeight() - controlBar.getHeight();
    double scale = GraphicsUtil.getScale(
        image.getWidth(), 
        image.getHeight(),
        maxWidth, 
        maxHeight);

    imageView.setImage(image);
    imageView.setFitWidth(maxWidth);
    imageView.setFitHeight(maxHeight);
    imageView.setPreserveRatio(true);
    
    double canvasWidth = scale * image.getWidth();
    double canvasHeight = scale * image.getHeight();

    canvasLayer.setWidth(canvasWidth);
    canvasLayer.setHeight(canvasHeight);

    Box box = landmarks.getBox();
    GraphicsContext graphicsContext = canvasLayer.getGraphicsContext2D();
    graphicsContext.clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());
    graphicsContext.setStroke(Color.BLUE);
    graphicsContext.setLineWidth(2);
    graphicsContext.strokeRect(box.getLeft() * scale, box.getTop() * scale, box.getWidth() * scale, box.getHeight() * scale);
    
    int positionNumber = 0;
    for (Position position : landmarks.getPositions())
    {
      graphicsContext.setStroke(getColor(positionNumber));
      GraphicsUtil.drawCross(graphicsContext, position.getX() * scale, position.getY() * scale);
      positionNumber++;
    }
  }
  
  public int getLandmarksIndex() 
  {
    return landmarksIndex;
  }
  
  public File getLandmarksDirectory()
  {
    return landmarksDirectory;
  }
  
  public void setLandmarksIndex(int newPosition)
  {
    if (newPosition < 0)
    {
      landmarksIndex = 0;
    }
    else if (newPosition >= landmarksList.size())
    {
      landmarksIndex = landmarksList.size() - 1;
    }
    else
    {
      landmarksIndex = newPosition;
    }
    landmarksIndexLabel.setIndex(landmarksIndex + 1);
    Landmarks landmarks = landmarksList.get(landmarksIndex);
    show(landmarks);
  }
}