package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import java.io.File;
import java.net.MalformedURLException;

import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;
import com.github.thomasfox.videowindmeasurement.xml.Box;
import com.github.thomasfox.videowindmeasurement.xml.Dataset;
import com.github.thomasfox.videowindmeasurement.xml.ImageMeta;
import com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlUtil;
import com.github.thomasfox.videowindmeasurement.xml.Part;

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
  
  private Dataset dataset;
  
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
    dataset = LandmarksXmlUtil.unmarshal(file);
    directory = file.getParentFile();
    forwardButton.setDataset(dataset);
    show(dataset.getImages().getImages().get(0));
  }
  
  public void show(ImageMeta imageMeta)
  {
    Image image;
    try
    {
      image = new Image(new File(directory, imageMeta.getFile()).toURI().toURL().toString());
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
    
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

    
    Box box = imageMeta.getBox();
    GraphicsContext graphicsContext = canvasLayer.getGraphicsContext2D();
    graphicsContext.clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());
    graphicsContext.setStroke(Color.BLUE);
    graphicsContext.setLineWidth(2);
    graphicsContext.strokeRect(box.getLeft() * scale, box.getTop() * scale, box.getWidth() * scale, box.getHeight() * scale);
    
    for (Part part : box.getParts())
    {
      if ("1".equals(part.getName()))
      {
        graphicsContext.setStroke(Color.BLACK);
      }
      else if ("2".equals(part.getName()))
      {
        graphicsContext.setStroke(Color.WHITE);
      }
      else 
      {
        graphicsContext.setStroke(Color.RED);
      }
      GraphicsUtil.drawCross(graphicsContext, part.getX() * scale, part.getY() * scale);
    }

  }
}