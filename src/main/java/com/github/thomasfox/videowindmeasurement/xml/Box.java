package com.github.thomasfox.videowindmeasurement.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.github.thomasfox.videowindmeasurement.fx.GraphicsUtil;

public class Box implements Cloneable
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
  
  public Box clone()
  {
    Box copy = new Box();
    copy.top = this.top;
    copy.left = this.left;
    copy.width = this.width;
    copy.height = this.height;
    copy.parts = new ArrayList<>();
    for (Part part : this.parts)
    {
      copy.parts.add(part.clone());
    }
    return copy;
  }
  
  public int getRotationBin(int numberOfBins)
  {
    if (parts.size() != 3)
    {
      return -1;
    }
    int middleX3 = parts.get(0).getX() + parts.get(1).getX() + parts.get(2).getX();
    int middleY3 = parts.get(0).getY() + parts.get(1).getY() + parts.get(2).getY();
    
    double angle1 = GraphicsUtil.getAngle(parts.get(0).getX() * 3 - middleX3, parts.get(0).getY() * 3 - middleY3);
    double angle2 = GraphicsUtil.getAngle(parts.get(1).getX() * 3 - middleX3, parts.get(1).getY() * 3 - middleY3) - 2 * Math.PI / 3;
    if (angle2 < 0) 
    {
      angle2 += 2 * Math.PI;
    }
    double angle3 = GraphicsUtil.getAngle(parts.get(2).getX() * 3 - middleX3, parts.get(2).getY() * 3 - middleY3) - 2 * Math.PI / 3;
    if (angle3 < 0) 
    {
      angle3 += 2 * Math.PI;
    }
    double result = (angle1 + angle2 + angle3) * numberOfBins / (6 * Math.PI);
    return new Double(result).intValue();
  }
}
