package com.github.thomasfox.videowindmeasurement.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Detection
{
  public int top;
  
  public int bottom;
  
  public int left;
  
  public int right;
  
  public int index;
  
  @JsonIgnore
  public int getMiddleX()
  {
    return (left + right) / 2;
  }
  
  @JsonIgnore
  public int getMiddleY()
  {
    return (top + bottom) / 2;
  }
  
  @JsonIgnore
  public int getHeight()
  {
    return Math.abs(top - bottom);
  }
  
  @JsonIgnore
  public int getWidth()
  {
    return Math.abs(right - left);
  }
}
