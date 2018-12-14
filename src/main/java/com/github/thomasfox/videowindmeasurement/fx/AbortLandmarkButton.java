package com.github.thomasfox.videowindmeasurement.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class AbortLandmarkButton extends Button
{
  private AbortLandmarkActionHandler abortlandmarkActionHandler;

  public AbortLandmarkButton(LandmarkButton landmarkButton)
  {
    super("Abort Landmark");
    abortlandmarkActionHandler = new AbortLandmarkActionHandler(landmarkButton);
    setOnAction(abortlandmarkActionHandler);
  }

  private static class AbortLandmarkActionHandler implements EventHandler<ActionEvent>
  {
    private LandmarkButton landmarkButton;
    
    public AbortLandmarkActionHandler(LandmarkButton landmarkButton)
    {
      this.landmarkButton = landmarkButton;
    }

    public void handle(ActionEvent e)
    {
      if (landmarkButton == null)
      {
        return;
      }
      landmarkButton.abortLandmark();
    }
  }
}
