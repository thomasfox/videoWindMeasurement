package com.github.thomasfox.videowindmeasurement.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class PlaySingleFrameButton extends Button
{
  private PlaySingleFrameActionHandler playSingleFrameActionHandler;

  public PlaySingleFrameButton()
  {
    super(">||");
    playSingleFrameActionHandler = new PlaySingleFrameActionHandler();
    setOnAction(playSingleFrameActionHandler);
  }
  
  public void setMediaPlayer(MediaPlayer mediaPlayer)
  {
    playSingleFrameActionHandler.setMediaPlayer(mediaPlayer);
  }

  private static class PlaySingleFrameActionHandler implements EventHandler<ActionEvent>
  {
    private MediaPlayer mediaPlayer;

    private boolean atEndOfMedia = false;

    public void handle(ActionEvent e)
    {
      if (mediaPlayer == null)
      {
        return;
      }

      Status status = mediaPlayer.getStatus();

      if (status == Status.UNKNOWN || status == Status.HALTED)
      {
        return;
      }

      if (status == Status.PAUSED
          || status == Status.READY
          || status == Status.STOPPED)
      {
        if (atEndOfMedia)
        {
          mediaPlayer.seek(mediaPlayer.getStartTime());
          atEndOfMedia = false;
        }
        mediaPlayer.play();
      }
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer)
    {
      this.mediaPlayer = mediaPlayer;
    }
    
    public void notifyAtEndOfMedia()
    {
      atEndOfMedia = true;
    }
  }
}
