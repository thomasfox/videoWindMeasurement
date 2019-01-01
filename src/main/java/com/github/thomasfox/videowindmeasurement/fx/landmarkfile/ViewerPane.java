package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import java.io.File;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;
import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;
import com.github.thomasfox.videowindmeasurement.xml.Box;
import com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

  private final ForwardButton forwardButton;
  
  private final HBox controlBar = new HBox();
  
  private List<Landmarks> dataset;
  
  private File directory;
  
  private Scene scene;
  
  public ViewerPane(Scene scene)
  {
    this.scene = scene;
    setStyle("-fx-background-color: #c8c8c8;");

    controlBar.setAlignment(Pos.CENTER);
    controlBar.setPadding(new Insets(5, 10, 5, 10));
    BorderPane.setAlignment(controlBar, Pos.CENTER);

    Pane imagePane = new Pane(imageView, canvasLayer);
    setCenter(imagePane);
 
    forwardButton = new ForwardButton(this);
    controlBar.getChildren().add(forwardButton);

    setBottom(controlBar);
  }
  
  public void loadFile(File file)
  {
    directory = file.getParentFile();
    dataset = LandmarksXmlUtil.toLandmarks(LandmarksXmlUtil.unmarshal(file), directory);
    forwardButton.setLandmarksList(dataset);
    show(dataset.get(0));
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
      if (positionNumber == 0)
      {
        graphicsContext.setStroke(Color.BLACK);
      }
      else if (positionNumber == 1)
      {
        graphicsContext.setStroke(Color.WHITE);
      }
      else 
      {
        graphicsContext.setStroke(Color.RED);
      }
      GraphicsUtil.drawCross(graphicsContext, position.getX() * scale, position.getY() * scale);
      positionNumber++;
    }
  }
}