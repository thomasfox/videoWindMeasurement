package com.github.thomasfox.videowindmeasurement.xml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class LandmarksXmlWriter
{
  public static final int NUMBER_OF_ROTATION_BINS = 3;
  
  private Dataset dataset = new Dataset();
 
  public LandmarksXmlWriter()
  {
    dataset.setName("Training for rotation detector");
    dataset.setComment("Created by VideoWindMeasurement");
   }
  
  public void addLandmarksAndSaveImages(List<Landmarks> landmarksList, File directory, int width, int height)
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
        ImageIO.write(image, "png", new File(directory, imageFileName));
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      Box box = landmarks.getBox();
      imageMeta.setBox(box);
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
    
  public void saveToXML(String xmlFilenamePrefix, File directory) 
  {
    marshal(new File(directory, xmlFilenamePrefix + ".xml"), dataset);
    saveRotationBinsToXML(xmlFilenamePrefix, directory);
  }
  
  public void saveRotationBinsToXML(String xmlFilenamePrefix, File directory)
  {
    for (int rotationBin = 0; rotationBin < NUMBER_OF_ROTATION_BINS; rotationBin++)
    {
      Dataset singleClassDataset = dataset.clone();
      for (ImageMeta image : singleClassDataset.getImages().getImages())
      {
        if (image.getBox().getRotationBin(NUMBER_OF_ROTATION_BINS) != rotationBin)
        {
          image.setBox(null);
        }
      }
      marshal(new File(directory, xmlFilenamePrefix + "_" + rotationBin + ".xml"), singleClassDataset);
    }
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
  
  public static List<Landmarks> toLandmarks(Dataset dataset, File directory)
  {
    List<Landmarks> result = new ArrayList<>();
    for (ImageMeta imageMeta : dataset.getImages().getImages())
    {
      Image image;
      try
      {
        image = new Image(new File(directory, imageMeta.getFile()).toURI().toURL().toString());
      }
      catch (MalformedURLException e)
      {
        throw new RuntimeException(e);
      }
      Landmarks landmarks = new Landmarks(image);
      for (Part part : imageMeta.getBox().getParts())
      {
        landmarks.getPositions().add(new Position(part.getX(), part.getY()));
      }
      result.add(landmarks);
    }
    return result;
  }
}
