package com.github.thomasfox.videowindmeasurement.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Part
{
  private String name;
  
  private int x;
  
  private int y;

  @XmlAttribute(required = true)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @XmlAttribute(required = true)
  public int getX()
  {
    return x;
  }

  public void setX(int x)
  {
    this.x = x;
  }

  @XmlAttribute(required = true)
  public int getY()
  {
    return y;
  }

  public void setY(int y)
  {
    this.y = y;
  }
}
