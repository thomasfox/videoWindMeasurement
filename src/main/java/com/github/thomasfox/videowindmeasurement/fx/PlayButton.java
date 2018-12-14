package com.github.thomasfox.videowindmeasurement.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class PlayButton extends Button
{
  private boolean stopRequested;

  private PlayActionHandler playActionHandler;

  public PlayButton()
  {
    super(">");
    playActionHandler = new PlayActionHandler();
    setOnAction(playActionHandler);
  }
  
  public void setMediaPlayer(MediaPlayer mediaPlayer)
  {
    mediaPlayer.setOnPlaying(new Runnable()
    {
      public void run()
      {
        if (stopRequested)
        {
          mediaPlayer.pause();
          stopRequested = false;
        }
        else
        {
          setText("||");
        }
      }
    });

    mediaPlayer.setOnPaused(new Runnable()
    {
      public void run()
      {
        setText(">");
      }
    });

    mediaPlayer.setOnEndOfMedia(new Runnable()
    {
      public void run()
      {
        setText(">");
        stopRequested = true;
        playActionHandler.notifyAtEndOfMedia();
      }
    });
    
    playActionHandler.setMediaPlayer(mediaPlayer);
  }

  private static class PlayActionHandler implements EventHandler<ActionEvent>
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
      else
      {
        mediaPlayer.pause();
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
