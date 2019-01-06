package com.github.thomasfox.videowindmeasurement.fx.landmarkfile;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class LandmarksIndex extends Label
{
  private int index = 0;
  
  private int maxIndex = 0;
  
  public LandmarksIndex()
  {
    setPrefWidth(50);
    setMinWidth(30);
    setTextAlignment(TextAlignment.CENTER);
  }

  public void setIndex(int index)
  {
    this.index = index;
    update();
  }

  public void setMaxIndex(int maxIndex)
  {
    this.maxIndex = maxIndex;
    update();
  }

  private void update()
  {
    setText(String.format("(%d/%d)", index, maxIndex));
  }
}
