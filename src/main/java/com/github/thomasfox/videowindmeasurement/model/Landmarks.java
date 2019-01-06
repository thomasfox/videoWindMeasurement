package com.github.thomasfox.videowindmeasurement.model;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasfox.videowindmeasurement.xml.Box;

import javafx.scene.image.Image;

public class Landmarks
{
  private static final double BOXSIZE_RADIUS_RATIO = 2.5d;
  
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
    Double boxLeftX = boxCenterX - (radius * BOXSIZE_RADIUS_RATIO / 2);
    Double boxTopY = boxCenterY - (radius * BOXSIZE_RADIUS_RATIO / 2);
    Box box = new Box();
    box.setTop(new Double(boxTopY).intValue());
    box.setLeft(new Double(boxLeftX).intValue());
    box.setHeight(new Double(BOXSIZE_RADIUS_RATIO * radius).intValue());
    box.setWidth(new Double(BOXSIZE_RADIUS_RATIO * radius).intValue());
    return box;

  }

  private Double getPositionCenterY()
  {
    Double boxCenterY = (positions.get(0).getY()
        + positions.get(1).getY() 
        + positions.get(2).getY()
        + 2 * positions.get(3).getY()) / 5d;
    return boxCenterY;
  }

  private Double getPositionCenterX()
  {
    Double boxCenterX = (positions.get(0).getX()
        + positions.get(1).getX() 
        + positions.get(2).getX()
        + 2 * positions.get(3).getX()) / 5d;
    return boxCenterX;
  }
  
  private double distance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
}
