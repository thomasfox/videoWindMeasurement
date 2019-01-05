package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class BackButton extends Button
{
  private BackActionHandler backActionHandler;

  public BackButton(ViewerPane viewerPane)
  {
    super("previous");
    backActionHandler = new BackActionHandler(viewerPane);
    setOnAction(backActionHandler);
  }
  
  private static class BackActionHandler implements EventHandler<ActionEvent>
  {
    private ViewerPane viewerPane;
    
    public BackActionHandler(ViewerPane viewerPane)
    {
      this.viewerPane = viewerPane;
    }

    public void handle(ActionEvent e)
    {
      viewerPane.setLandmarksIndex(viewerPane.getLandmarksIndex() - 1);
   }
  }
}
