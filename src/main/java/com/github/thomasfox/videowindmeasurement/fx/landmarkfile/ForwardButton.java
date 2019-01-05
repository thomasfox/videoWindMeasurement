package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ForwardButton extends Button
{
  private ForwardActionHandler forwardActionHandler;

  public ForwardButton(ViewerPane viewerPane)
  {
    super("next");
    forwardActionHandler = new ForwardActionHandler(viewerPane);
    setOnAction(forwardActionHandler);
  }
  
  private static class ForwardActionHandler implements EventHandler<ActionEvent>
  {
    private ViewerPane viewerPane;
    
    public ForwardActionHandler(ViewerPane viewerPane)
    {
      this.viewerPane = viewerPane;
    }

    public void handle(ActionEvent e)
    {
      viewerPane.setLandmarksIndex(viewerPane.getLandmarksIndex() + 1);
    }
  }
}
