package com.github.thomasfox.videowindmeasurement.fx.video;

import javafx.scene.control.Label;
import javafx.util.Duration;

public class PlayTime extends Label
{
  private Duration duration = Duration.ZERO;

  private Duration currentTime = Duration.ZERO;
  
  public PlayTime()
  {
    setPrefWidth(130);
    setMinWidth(50);
  }

  public void setDuration(Duration duration)
  {
    this.duration = duration;
    update();
  }

  public void setCurrentTime(Duration currentTime)
  {
    this.currentTime = currentTime;
    update();
  }

  private void update()
  {
    int currentTimeSeconds = (int) Math.floor(currentTime.toSeconds());
    int currentTimeHours = currentTimeSeconds / (60 * 60);
    if (currentTimeHours > 0)
    {
      currentTimeSeconds -= currentTimeHours * 60 * 60;
    }
    int currentTimeMinutes = currentTimeSeconds / 60;
    currentTimeSeconds = currentTimeSeconds - currentTimeHours * 60 * 60
        - currentTimeMinutes * 60;

    if (duration.greaterThan(Duration.ZERO))
    {
      int intDuration = (int) Math.floor(duration.toSeconds());
      int durationHours = intDuration / (60 * 60);
      if (durationHours > 0)
      {
        intDuration -= durationHours * 60 * 60;
      }
      int durationMinutes = intDuration / 60;
      int durationSeconds = intDuration - durationHours * 60 * 60
          - durationMinutes * 60;
      if (durationHours > 0)
      {
        setText(String.format("%d:%02d:%02d/%d:%02d:%02d",
            currentTimeHours, currentTimeMinutes, currentTimeSeconds,
            durationHours, durationMinutes, durationSeconds));
      }
      else
      {
        setText(String.format("%02d:%02d/%02d:%02d",
            currentTimeMinutes, currentTimeSeconds, durationMinutes,
            durationSeconds));
      }
    }
    else
    {
      if (currentTimeHours > 0)
      {
        setText(String.format("%d:%02d:%02d", currentTimeHours,
            currentTimeMinutes, currentTimeSeconds));
      }
      else
      {
        setText(String.format("%02d:%02d", currentTimeMinutes,
            currentTimeSeconds));
      }
    }
  }

}
