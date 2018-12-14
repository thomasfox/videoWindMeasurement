package com.github.thomasfox.videowindmeasurement.model;

public class Position
{
  private int x;
  
  private int y;

  public Position(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public Position(double x, double y)
  {
    this.x = new Double(x).intValue();
    this.y = new Double(y).intValue();
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }
}
