package com.github.thomasfox.videowindmeasurement.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Box
{
  private Integer top;

  private Integer left;

  private Integer width;

  private Integer height;

  private List<Part> parts = new ArrayList<>();

  @XmlAttribute(required = true)
  public Integer getTop()
  {
    return top;
  }

  public void setTop(Integer top)
  {
    this.top = top;
  }

  @XmlAttribute(required = true)
  public Integer getLeft()
  {
    return left;
  }

  public void setLeft(Integer left)
  {
    this.left = left;
  }

  @XmlAttribute(required = true)
  public Integer getWidth()
  {
    return width;
  }

  public void setWidth(Integer width)
  {
    this.width = width;
  }

  @XmlAttribute(required = true)
  public Integer getHeight()
  {
    return height;
  }

  public void setHeight(Integer height)
  {
    this.height = height;
  }

  @XmlElement(name = "part")
  public List<Part> getParts()
  {
    return parts;
  }

  public void setParts(List<Part> part)
  {
    this.parts = part;
  }
}
