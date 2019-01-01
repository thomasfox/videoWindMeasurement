package com.github.thomasfox.videowindmeasurement.fx.video;

import java.io.File;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.xml.LandmarksXmlWriter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SaveLandmarksButton extends Button
{
  private SaveLandmarkActionHandler savelandmarkActionHandler;
  
  public SaveLandmarksButton()
  {
    super("Save Landmarks");
    savelandmarkActionHandler = new SaveLandmarkActionHandler();
    setOnAction(savelandmarkActionHandler);
  }
  
  public void setLandmarksList(List<Landmarks> landmarksList)
  {
    savelandmarkActionHandler.setLandmarksList(landmarksList);
  }

  public void setImageWidthAndHeight(int imageWidth, int imageHeight)
  {
    savelandmarkActionHandler.setImageWidthAndHeight(imageWidth, imageHeight);
  }
  
  public void setDirectory(File directory)
  {
    savelandmarkActionHandler.setDirectory(directory);
  }

  private static class SaveLandmarkActionHandler implements EventHandler<ActionEvent>
  {
    private List<Landmarks> landmarksList;
    
    private int imageWidth;
    
    private int imageHeight;
    
    private File directory;

    public void setLandmarksList(List<Landmarks> landmarksList)
    {
      this.landmarksList = landmarksList;
    }

    public void setDirectory(File directory)
    {
      this.directory = directory;
    }

    public void setImageWidthAndHeight(int imageWidth, int imageHeight)
    {
      this.imageWidth = imageWidth;
      this.imageHeight = imageHeight;
    }

    public void handle(ActionEvent e)
    {
      LandmarksXmlWriter writer = new LandmarksXmlWriter();
      writer.addLandmarksAndSaveImages(landmarksList, directory, imageWidth, imageHeight);
      writer.saveToXML("landmarks", directory);
    }
  }
}
