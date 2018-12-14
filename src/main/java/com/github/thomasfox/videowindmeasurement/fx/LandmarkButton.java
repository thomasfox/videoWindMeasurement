package com.github.thomasfox.videowindmeasurement.fx;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

public class LandmarkButton extends Button
{
  private LandmarkActionHandler landmarkActionHandler;

  public LandmarkButton(List<Landmarks> landmarksList, PlayButton playButton)
  {
    super("Landmark");
    landmarkActionHandler = new LandmarkActionHandler(landmarksList, playButton);
    setOnAction(landmarkActionHandler);
  }
  
  public void setMediaView(MediaView mediaView)
  {
    landmarkActionHandler.setMediaView(mediaView);
  }

  public void setCanvasLayer(Canvas canvasLayer)
  {
    landmarkActionHandler.setCanvasLayer(canvasLayer);
  }
  
  public void abortLandmark()
  {
    landmarkActionHandler.abortLandmark();
  }

  private static class LandmarkActionHandler implements EventHandler<ActionEvent>
  {
    private MediaView mediaView;
    
    private Canvas canvasLayer;
    
    private PlayButton playButton;
    
    private int markIndex;
    
    private List<Landmarks> landmarksList;
    
    private Landmarks currentLandmarks;
    
    public LandmarkActionHandler(List<Landmarks> landmarksList, PlayButton playButton)
    {
      this.landmarksList = landmarksList;
      this.playButton = playButton;
    }

    public void setCanvasLayer(Canvas canvasLayer)
    {
      this.canvasLayer = canvasLayer;
    }

    public void abortLandmark()
    {
      if (mediaView == null)
      {
        return;
      }
      playButton.setDisable(false);
      canvasLayer.setOnMouseClicked(null);
      GraphicsContext gc1 = canvasLayer.getGraphicsContext2D();
      gc1.clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());
      markIndex = 0;
      currentLandmarks = null;
    }

    public void handle(ActionEvent e)
    {
      if (mediaView == null)
      {
        return;
      }
      playButton.setDisable(true);
      
      if (currentLandmarks == null)
      {
        WritableImage image = new WritableImage(
            mediaView.getMediaPlayer().getMedia().getWidth(),
            mediaView.getMediaPlayer().getMedia().getHeight());
        mediaView.snapshot(null, image);
        
        double scale = Math.min(
            mediaView.getFitWidth() / image.getWidth(), 
            mediaView.getFitHeight() / image.getHeight());
        
        double canvasWidth = scale * image.getWidth();
        double canvasHeight = scale * image.getHeight();
        canvasLayer.setWidth(canvasWidth);
        canvasLayer.setHeight(canvasHeight);
        
        canvasLayer.setOnMouseClicked((event) -> {
          GraphicsContext gc1 = canvasLayer.getGraphicsContext2D();
          if (markIndex == 0)
          {
            gc1.setStroke(Color.BLACK);
          }
          else if (markIndex == 1)
          {
            gc1.setStroke(Color.WHITE);
          }
          else 
          {
            gc1.setStroke(Color.RED);
          }
          gc1.strokeLine(event.getX() - 20, event.getY(), event.getX() + 20, event.getY());
          gc1.strokeLine(event.getX(), event.getY() - 20, event.getX(), event.getY() + 20);
          markIndex ++;
          currentLandmarks.getPositions().add(new Position(event.getX() / scale, event.getY() / scale));
          if (markIndex > 2)
          {
            landmarksList.add(currentLandmarks);
            abortLandmark();
          }
        });
        currentLandmarks = new Landmarks(image);
      }

    }

    public void setMediaView(MediaView mediaView)
    {
      this.mediaView = mediaView;
    }
  }
}
