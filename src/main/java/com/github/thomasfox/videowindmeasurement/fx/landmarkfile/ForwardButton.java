package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;

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
  
  public void setLandmarksList(List<Landmarks> landmarksList)
  {
    forwardActionHandler.setLandmarksList(landmarksList);
  }
  
  private static class ForwardActionHandler implements EventHandler<ActionEvent>
  {
    private ViewerPane viewerPane;
    
    private List<Landmarks> landmarksList;
    
    private int position = 0;
    
    public ForwardActionHandler(ViewerPane viewerPane)
    {
      this.viewerPane = viewerPane;
    }

    public void handle(ActionEvent e)
    {
      if (position >= landmarksList.size() - 1)
      {
        return;
      }
      position++;
      Landmarks landmarks = landmarksList.get(position);
      viewerPane.show(landmarks);
    }

    public void setLandmarksList(List<Landmarks> landmarksList)
    {
      this.landmarksList = landmarksList;
    }
  }
}
