package com.github.thomasfox.videowindmeasurement.fx.video;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class PlayerPane extends BorderPane
{
  private MediaPlayer mediaPlayer;
  
  private final PlayButton playButton;

  private final TimeSlider timeSlider;

  private final PlayTime playTime;

  private final VolumeSlider volumeSlider;
  
  private final LandmarkButton landmarkButton;
  
  private final AbortLandmarkButton abortLandmarkButton;
  
  private final SaveLandmarksButton saveLandmarksButton;
  
  private final DetectButton detectButton;

  private final HBox controlBar = new HBox();
  
  private List<Landmarks> landmarksList = new ArrayList<>();

  public PlayerPane()
  {
    setStyle("-fx-background-color: #c8c8c8;");

    controlBar.setAlignment(Pos.CENTER);
    controlBar.setPadding(new Insets(5, 10, 5, 10));
    BorderPane.setAlignment(controlBar, Pos.CENTER);

    playButton = new PlayButton();
    controlBar.getChildren().add(playButton);

    Label spacer = new Label("     ");
    controlBar.getChildren().add(spacer);

    timeSlider = new TimeSlider(controlBar);

    playTime = new PlayTime();
    controlBar.getChildren().add(playTime);

    volumeSlider = new VolumeSlider(controlBar);
    
    landmarkButton = new LandmarkButton(playButton);
    landmarkButton.setLandmarksList(landmarksList);
    controlBar.getChildren().add(landmarkButton);

    abortLandmarkButton = new AbortLandmarkButton(landmarkButton);
    controlBar.getChildren().add(abortLandmarkButton);

    saveLandmarksButton = new SaveLandmarksButton();
    saveLandmarksButton.setLandmarksList(landmarksList);
    controlBar.getChildren().add(saveLandmarksButton);

    detectButton = new DetectButton(playButton);
    controlBar.getChildren().add(detectButton);

    setBottom(controlBar);
  }

  protected void videoFrameChanged()
  {
    if (playTime != null && timeSlider != null && volumeSlider != null)
    {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          Duration currentTime = mediaPlayer.getCurrentTime();
          playTime.setCurrentTime(currentTime);
          timeSlider.setCurrentTime(currentTime);
          if (!volumeSlider.isValueChanging())
          {
            volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume() * 100));
          }
        }
      });
    }
  }
  
  public void loadFile(File file)
  {
    Media media;
    try
    {
      media = new Media(file.toURI().toURL().toString());
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
    if (mediaPlayer != null)
    {
      mediaPlayer.stop();
      mediaPlayer.dispose();
    }
    mediaPlayer = new MediaPlayer(media);
    MediaView mediaView = new MediaView(mediaPlayer);
    Canvas canvasLayer = new Canvas();

    Pane videoPane = new Pane(mediaView, canvasLayer);
    videoPane.setStyle("-fx-background-color: black;");
    setCenter(videoPane);

    mediaPlayer.currentTimeProperty().addListener(new InvalidationListener()
    {
      public void invalidated(Observable ov)
      {
        videoFrameChanged();
      }
    });

    mediaPlayer.setOnReady(new Runnable()
    {
      public void run()
      {
        Duration duration = mediaPlayer.getMedia().getDuration();
        playTime.setDuration(duration);
        timeSlider.setDuration(duration);
        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(mediaView.getScene().getWidth());
        mediaView.setFitHeight(mediaView.getScene().getHeight() - controlBar.getHeight());
        saveLandmarksButton.setImageWidthAndHeight(
            mediaView.getMediaPlayer().getMedia().getWidth(),
            mediaView.getMediaPlayer().getMedia().getHeight());
      }
    });

    mediaPlayer.setCycleCount(1);
    
    playButton.setMediaPlayer(mediaPlayer);
    timeSlider.setMediaPlayer(mediaPlayer);
    volumeSlider.setMediaPlayer(mediaPlayer);
    landmarkButton.setMediaView(mediaView);
    landmarkButton.setCanvasLayer(canvasLayer);
    detectButton.setMediaView(mediaView);
    detectButton.setCanvasLayer(canvasLayer);
  }
  
  public void setLandmarksList(List<Landmarks> landmarksList)
  {
    this.landmarksList = landmarksList;
    landmarkButton.setLandmarksList(landmarksList);
    saveLandmarksButton.setLandmarksList(landmarksList);
  }
}