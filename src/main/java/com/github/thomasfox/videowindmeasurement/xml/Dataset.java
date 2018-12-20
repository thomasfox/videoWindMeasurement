package com.github.thomasfox.videowindmeasurement.xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "dataset")
@XmlType(propOrder={"name","comment", "images"})
public class Dataset implements Cloneable
{
  private String name;
  
  private String comment;
  
  private Images images = new Images();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public Images getImages()
  {
    return images;
  }

  public void setImages(Images images)
  {
    this.images = images;
  }

  public Dataset clone()
  {
    Dataset copy = new Dataset();
    copy.name = this.name;
    copy.comment = this.comment;
    copy.images = this.images.clone();
    return copy;
  }
}
