package com.github.thomasfox.videowindmeasurement.fx;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class TimeSlider extends Slider
{
  private Duration duration;
  
  private ValueChangedListener valueChangedListener = new ValueChangedListener(this);
  
  public TimeSlider(HBox parent)
  {
    Label timeLabel = new Label("Time: ");
    parent.getChildren().add(timeLabel);

    HBox.setHgrow(this, Priority.ALWAYS);
    setMinWidth(50);
    setMaxWidth(Double.MAX_VALUE);
    valueProperty().addListener(valueChangedListener);
    parent.getChildren().add(this);
  }
  
  public void setDuration(Duration duration)
  {
    this.duration = duration;
    setDisable(duration.isUnknown());
    valueChangedListener.setDuration(duration);
  }
  
  public void setCurrentTime(Duration currentTime)
  {
    if (!isDisabled()
        && duration.greaterThan(Duration.ZERO)
        && !isValueChanging())
    {
      setValue(currentTime.divide(duration).toMillis() * 100.0);
    }
  }
  
  public void setMediaPlayer(MediaPlayer mediaPlayer)
  {
    valueChangedListener.setMediaPlayer(mediaPlayer);
  }
  
  
  private static class ValueChangedListener implements InvalidationListener
  {
    private TimeSlider slider;
    
    private MediaPlayer mediaPlayer;
    
    private Duration duration;
    
    public ValueChangedListener(TimeSlider slider)
    {
      this.slider = slider;
    }
    
    public void setDuration(Duration duration)
    {
      this.duration = duration;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer)
    {
      this.mediaPlayer = mediaPlayer;
    }
    
    public void invalidated(Observable ov) 
    {  
      if (slider.isValueChanging()) {
        mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
      }
    }
  }
}
