package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import java.util.List;

import com.github.thomasfox.videowindmeasurement.xml.Dataset;
import com.github.thomasfox.videowindmeasurement.xml.ImageMeta;

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
  
  public void setDataset(Dataset dataset)
  {
    forwardActionHandler.setDataset(dataset);
  }
  
  private static class ForwardActionHandler implements EventHandler<ActionEvent>
  {
    private ViewerPane viewerPane;
    
    private Dataset dataset;
    
    private int position = 0;
    
    public ForwardActionHandler(ViewerPane viewerPane)
    {
      this.viewerPane = viewerPane;
    }

    public void handle(ActionEvent e)
    {
      List<ImageMeta> imageList = dataset.getImages().getImages();
      if (position >= imageList.size() - 1)
      {
        return;
      }
      position++;
      ImageMeta image = imageList.get(position);
      viewerPane.show(image);
    }

    public void setDataset(Dataset dataset)
    {
      this.dataset = dataset;
    }
  }
}
