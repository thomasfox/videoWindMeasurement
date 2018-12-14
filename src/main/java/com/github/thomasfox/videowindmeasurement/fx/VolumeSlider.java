package com.github.thomasfox.videowindmeasurement.fx;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;

public class VolumeSlider extends Slider
{
  private VolumeChangedListener volumeChangedListener = new VolumeChangedListener(this);
  
  public VolumeSlider (HBox parent)
  {
    Label volumeLabel = new Label("Vol: ");
    parent.getChildren().add(volumeLabel);

    setPrefWidth(70);
    setMaxWidth(Region.USE_PREF_SIZE);
    setMinWidth(30);
    valueProperty().addListener(volumeChangedListener);
    parent.getChildren().add(this);
  }

  public void setMediaPlayer(MediaPlayer mediaPlayer)
  {
    volumeChangedListener.setMediaPlayer(mediaPlayer);
  }
  
  static final class VolumeChangedListener implements InvalidationListener
  {
    private VolumeSlider volumeSlider;
    
    private MediaPlayer mediaPlayer;
    
    public VolumeChangedListener(VolumeSlider volumeSlider)
    {
      this.volumeSlider = volumeSlider;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer)
    {
      this.mediaPlayer = mediaPlayer;
    }

    public void invalidated(Observable ov)
    {
      if (volumeSlider.isValueChanging())
      {
        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
      }
    }
    
  }
}
