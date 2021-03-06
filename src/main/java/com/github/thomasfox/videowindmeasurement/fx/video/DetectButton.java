package com.github.thomasfox.videowindmeasurement.fx.video;

import static com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil.getColor;
import static com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlWriter.NUMBER_OF_ROTATION_BINS;

import com.github.thomasfox.videowindmeasurement.client.DetectionWebserviceClient;
import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;
import com.github.thomasfox.videowindmeasurement.json.DetectedImage;
import com.github.thomasfox.videowindmeasurement.json.DetectedImageList;
import com.github.thomasfox.videowindmeasurement.json.Detection;

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

public class DetectButton extends Button
{
  private DetectActionHandler detectActionHandler;

  public DetectButton(PlayButton playButton)
  {
    super("Detect");
    detectActionHandler = new DetectActionHandler(playButton);
    setOnAction(detectActionHandler);
  }
  
  public void setMediaView(MediaView mediaView)
  {
    detectActionHandler.setMediaView(mediaView);
  }

  public void setCanvasLayer(Canvas canvasLayer)
  {
    detectActionHandler.setCanvasLayer(canvasLayer);
  }
  
  private static class DetectActionHandler implements EventHandler<ActionEvent>
  {
    private MediaView mediaView;
    
    private Canvas canvasLayer;
    
    private PlayButton playButton;
    
    public DetectActionHandler(PlayButton playButton)
    {
      this.playButton = playButton;
    }

    public void setCanvasLayer(Canvas canvasLayer)
    {
      this.canvasLayer = canvasLayer;
    }

    public void handle(ActionEvent event)
    {
      if (mediaView == null)
      {
        return;
      }
      playButton.setDisable(true);
      
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
      GraphicsContext graphicsContext = canvasLayer.getGraphicsContext2D();
      graphicsContext.clearRect(0, 0, canvasLayer.getWidth(), canvasLayer.getHeight());

      DetectedImageList imageList = DetectionWebserviceClient.detectInImage(image);
      for (DetectedImage detectedImage  : imageList)
      {
        for (Detection detection : detectedImage.detections)
        {
          if (detection.right > mediaWidth || detection.bottom > mediaHeight)
          {
            continue;
          }
          graphicsContext.setStroke(Color.BLUE);
          graphicsContext.setLineWidth(2);
          graphicsContext.strokeRect(
              detection.left * scale,
              detection.top * scale,
              (detection.right - detection.left) * scale,
              (detection.bottom - detection.top) * scale);
          
          graphicsContext.setLineWidth(2);
          graphicsContext.setStroke(getColor(detection.index, NUMBER_OF_ROTATION_BINS));
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale + 2, detection.getMiddleY() * scale,
              detection.getMiddleX() * scale + 2, detection.top * scale);
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale + 2, detection.getMiddleY() * scale,
              detection.right * scale, (detection.getMiddleY() + 0.2887 * detection.getWidth()) * scale);
          
          graphicsContext.setStroke(getColor(detection.index + 1, NUMBER_OF_ROTATION_BINS));
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale, detection.getMiddleY() * scale,
              detection.left * scale, (detection.getMiddleY() + 0.2887 * detection.getWidth()) * scale);
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale, detection.getMiddleY() * scale,
              detection.getMiddleX() * scale, detection.top * scale);

          graphicsContext.setStroke(getColor(detection.index + 2, NUMBER_OF_ROTATION_BINS));
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale + 2, detection.getMiddleY() * scale + 2,
              detection.right * scale, (detection.getMiddleY() + 0.2887 * detection.getWidth()) * scale + 2);
          graphicsContext.strokeLine(
              detection.getMiddleX() * scale, detection.getMiddleY() * scale + 2,
              detection.left * scale, (detection.getMiddleY() + 0.2887 * detection.getWidth()) * scale + 2);
        }
      }
    }
    
    public void setMediaView(MediaView mediaView)
    {
      this.mediaView = mediaView;
    }
  }
} 
