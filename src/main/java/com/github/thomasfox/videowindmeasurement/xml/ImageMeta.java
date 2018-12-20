package com.github.thomasfox.videowindmeasurement.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class ImageMeta implements Cloneable
{
  private String file;
  
  private Box box;

  @XmlAttribute(required = true)
  public String getFile()
  {
    return file;
  }

  public void setFile(String file)
  {
    this.file = file;
  }

  public Box getBox()
  {
    return box;
  }

  public void setBox(Box box)
  {
    this.box = box;
  }
  
  
  public ImageMeta clone()
  {
    ImageMeta copy = new ImageMeta();
    copy.file = this.file;
    copy.box = box.clone();
    return copy;
  }
}
