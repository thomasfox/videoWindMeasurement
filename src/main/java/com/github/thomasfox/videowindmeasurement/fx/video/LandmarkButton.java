package com.github.thomasfox.videowindmeasurement.fx.video;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;
import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class LandmarkButton extends Button
{
  private LandmarkActionHandler landmarkActionHandler;

  public LandmarkButton(PlayButton playButton)
  {
    super("Landmark");
    landmarkActionHandler = new LandmarkActionHandler(playButton);
    setOnAction(landmarkActionHandler);
  }
  
  public void setLandmarksList(List<Landmarks> landmarksList)
  {
    landmarkActionHandler.setLandmarksList(landmarksList);
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
    
    public LandmarkActionHandler(PlayButton playButton)
    {
      this.playButton = playButton;
    }

    public void setLandmarksList(List<Landmarks> landmarksList)
    {
      this.landmarksList = landmarksList;
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
        double mediaWidth = mediaView.getMediaPlayer().getMedia().getWidth();
        double mediaHeight = mediaView.getMediaPlayer().getMedia().getHeight();
        
        double scale = GraphicsUtil.getScale(mediaWidth, mediaHeight, mediaView.getFitWidth(), mediaView.getFitHeight());
        
        double canvasWidth = scale * mediaWidth;
        double canvasHeight = scale * mediaHeight;
        
        WritableImage image = new WritableImage(
            new Double(canvasWidth).intValue(),
            new Double(canvasHeight).intValue());
        final SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setTransform(Transform.scale(1/scale, 1/scale));
        mediaView.snapshot(snapshotParameters, image);
        
        canvasLayer.setWidth(canvasWidth);
        canvasLayer.setHeight(canvasHeight);
        
        canvasLayer.setOnMouseClicked((event) -> {
          GraphicsContext graphicsContext = canvasLayer.getGraphicsContext2D();
          if (markIndex == 0)
          {
            graphicsContext.setStroke(Color.BLACK);
          }
          else if (markIndex == 1)
          {
            graphicsContext.setStroke(Color.WHITE);
          }
          else 
          {
            graphicsContext.setStroke(Color.RED);
          }
          GraphicsUtil.drawCross(graphicsContext, event.getX(), event.getY());
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
