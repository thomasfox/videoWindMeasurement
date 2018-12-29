package com.github.thomasfox.videowindmeasurement.xml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;

import javafx.embed.swing.SwingFXUtils;

public class LandmarksXmlUtil
{
  private Dataset dataset = new Dataset();
 
  public LandmarksXmlUtil()
  {
    dataset.setName("Training for rotation detector");
    dataset.setComment("Created by VideoWindMeasurement");
   }
  
  public void addLandmarks(List<Landmarks> landmarksList, int width, int height)
  {
    int imageCounter = 1;
    for (Landmarks landmarks : landmarksList)
    {
      ImageMeta imageMeta = new ImageMeta();
      dataset.getImages().getImages().add(imageMeta);
      String imageFileName = "image_" + imageCounter + ".png";
      imageMeta.setFile(imageFileName);
      imageCounter++;
      try
      {
        BufferedImage image = SwingFXUtils.fromFXImage(landmarks.getImage(), null);
        if (image.getWidth() > width || image.getHeight() > height)
        {
          image = image.getSubimage(0, 0, width, height);
        }
        ImageIO.write(image, "png", new File(imageFileName));
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      Double boxCenterX = (landmarks.getPositions().get(0).getX()
          + landmarks.getPositions().get(1).getX() 
          + landmarks.getPositions().get(2).getX()) / 3d;
      Double boxCenterY = (landmarks.getPositions().get(0).getY()
          + landmarks.getPositions().get(1).getY() 
          + landmarks.getPositions().get(2).getY()) / 3d;
      double radius = (distance(landmarks.getPositions().get(0).getX(), landmarks.getPositions().get(0).getY(), boxCenterX, boxCenterY)
          + distance(landmarks.getPositions().get(1).getX(), landmarks.getPositions().get(1).getY(), boxCenterX, boxCenterY)
          + distance(landmarks.getPositions().get(2).getX(), landmarks.getPositions().get(2).getY(), boxCenterX, boxCenterY)) / 3;
      Double boxLeftX = boxCenterX - radius;
      Double boxTopY = boxCenterY - radius;
      Box box = new Box();
      imageMeta.setBox(box);
      box.setTop(new Double(boxTopY).intValue());
      box.setLeft(new Double(boxLeftX).intValue());
      box.setHeight(new Double(2.5 * radius).intValue());
      box.setWidth(new Double(2.5 * radius).intValue());
      int partCount = 1;
      for (Position position : landmarks.getPositions())
      {
        Part part = new Part();
        box.getParts().add(part);
        part.setName(Integer.toString(partCount));
        part.setX(position.getX());
        part.setY(position.getY());
        partCount++;
      }
    }
  }
    
  public void saveToXML(String xmlFilenamePrefix) 
  {
    marshal(new File(xmlFilenamePrefix + ".xml"), dataset);
    saveRotationBinsToXML(xmlFilenamePrefix);
  }
  
  public void saveRotationBinsToXML(String xmlFilenamePrefix)
  {
    for (int rotationBin=0; rotationBin < 3; rotationBin++)
    {
      Dataset singleClassDataset = dataset.clone();
      for (ImageMeta image : singleClassDataset.getImages().getImages())
      {
        if (image.getBox().getRotationBin(3) != rotationBin)
        {
          image.setBox(null);
        }
      }
      marshal(new File(xmlFilenamePrefix + "_" + rotationBin + ".xml"), singleClassDataset);
    }
  }

  private double distance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  public static void marshal(File xmlFile, Object jaxbElement)
  {
    try
    {
      JAXBContext jaxbContext = JAXBContext.newInstance(jaxbElement.getClass());
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(jaxbElement, xmlFile);
    }
    catch (JAXBException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public static Dataset unmarshal(File xmlFile)
  {
    try
    {
      JAXBContext jaxbContext = JAXBContext.newInstance(Dataset.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      return (Dataset) unmarshaller.unmarshal(xmlFile);
    }
    catch (JAXBException e)
    {
      throw new RuntimeException(e);
    }
  }
}
