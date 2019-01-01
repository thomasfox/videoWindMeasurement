package com.github.thomasfox.videowindmeasurement.model;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.xml.Box;

import javafx.scene.image.Image;

public class Landmarks
{
  private List<Position> positions = new ArrayList<>();
  
  private Image image;

  public Landmarks(Image image)
  {
    this.image = image;
  }

  public List<Position> getPositions()
  {
    return positions;
  }

  public Image getImage()
  {
    return image;
  }
  
  public Box getBox()
  {
    Double boxCenterX = getPositionCenterX();
    Double boxCenterY = getPositionCenterY();
    double radius = (distance(positions.get(0).getX(), positions.get(0).getY(), boxCenterX, boxCenterY)
        + distance(positions.get(1).getX(), positions.get(1).getY(), boxCenterX, boxCenterY)
        + distance(positions.get(2).getX(), positions.get(2).getY(), boxCenterX, boxCenterY)) / 3;
    Double boxLeftX = boxCenterX - radius;
    Double boxTopY = boxCenterY - radius;
    Box box = new Box();
    box.setTop(new Double(boxTopY).intValue());
    box.setLeft(new Double(boxLeftX).intValue());
    box.setHeight(new Double(2.5 * radius).intValue());
    box.setWidth(new Double(2.5 * radius).intValue());
    return box;

  }

  private Double getPositionCenterY()
  {
    Double boxCenterY = (positions.get(0).getY()
        + positions.get(1).getY() 
        + positions.get(2).getY()) / 3d;
    return boxCenterY;
  }

  private Double getPositionCenterX()
  {
    Double boxCenterX = (positions.get(0).getX()
        + positions.get(1).getX() 
        + positions.get(2).getX()) / 3d;
    return boxCenterX;
  }
  
  private double distance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
}
