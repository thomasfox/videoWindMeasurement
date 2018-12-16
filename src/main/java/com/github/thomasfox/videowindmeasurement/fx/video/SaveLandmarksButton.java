package com.github.thomasfox.videowindmeasurement.fx.video;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlUtil;

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
  
  public void setImageWidthAndHeight(int imageWidth, int imageHeight)
  {
    savelandmarkActionHandler.setImageWidthAndHeight(imageWidth, imageHeight);
  }

  private static class SaveLandmarkActionHandler implements EventHandler<ActionEvent>
  {
    private List<Landmarks> landmarksList;
    
    private int imageWidth;
    
    private int imageHeight;

    public void setImageWidthAndHeight(int imageWidth, int imageHeight)
    {
      this.imageWidth = imageWidth;
      this.imageHeight = imageHeight;
    }

    public SaveLandmarkActionHandler(List<Landmarks> landmarksList)
    {
      this.landmarksList = landmarksList;
    }

    public void handle(ActionEvent e)
    {
      LandmarksXmlUtil writer = new LandmarksXmlUtil();
      writer.addLandmarks(landmarksList, imageWidth, imageHeight);
      writer.saveToXML("landmarks.xml");
    }
  }
}
