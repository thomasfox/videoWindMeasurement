package com.github.thomasfox.videowindmeasurement.fx;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlWriter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SaveLandmarksButton extends Button
{
  private SaveLandmarkActionHandler savelandmarkActionHandler;

  public SaveLandmarksButton(List<Landmarks> landmarksList)
  {
    super("Save Landmarks");
    savelandmarkActionHandler = new SaveLandmarkActionHandler(landmarksList);
    setOnAction(savelandmarkActionHandler);
  }

  private static class SaveLandmarkActionHandler implements EventHandler<ActionEvent>
  {
    private List<Landmarks> landmarksList;
    
    public SaveLandmarkActionHandler(List<Landmarks> landmarksList)
    {
      this.landmarksList = landmarksList;
    }

    public void handle(ActionEvent e)
    {
      LandmarksXmlWriter writer = new LandmarksXmlWriter();
      writer.addLandmarks(landmarksList);
      writer.saveToXML("landmarks.xml");
    }
  }
}
