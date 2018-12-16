package com.github.thomasfox.videowindmeasurement.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Images
{
  private List<ImageMeta> images = new ArrayList<>();

  @XmlElement(name = "image")
  public List<ImageMeta> getImages()
  {
    return images;
  }

  public void setImages(List<ImageMeta> images)
  {
    this.images = images;
  }
}
