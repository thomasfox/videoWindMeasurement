package com.github.thomasfox.videowindmeasurement.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.WritableImage;

public class Landmarks
{
  private List<Position> positions = new ArrayList<>();
  
  private WritableImage image;

  public Landmarks(WritableImage image)
  {
    this.image = image;
  }

  public List<Position> getPositions()
  {
    return positions;
  }

  public WritableImage getImage()
  {
    return image;
  }
}
