package com.github.thomasfox.videowindmeasurement.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.thomasfox.videowindmeasurement.model.Landmarks;
import com.github.thomasfox.videowindmeasurement.model.Position;

import javafx.embed.swing.SwingFXUtils;

public class LandmarksXmlWriter
{
  private Document dom;
  
  Element rootElement;
  
  public LandmarksXmlWriter()
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder db = dbf.newDocumentBuilder();
      dom = db.newDocument();
      rootElement = dom.createElement("dataset");
      dom.appendChild(rootElement);
      addTextElement(rootElement, "name", "Training for rotation detector");
      addTextElement(rootElement, "comment", "Created by VideoWindMeasurement");
    }
    catch (ParserConfigurationException e)
    {
      throw new RuntimeException(e);
    }
   }
  
  public void addLandmarks(List<Landmarks> landmarksList)
  {
    Element imagesElement = addElement(rootElement, "images");
    int imageCounter = 1;
    for (Landmarks landmarks : landmarksList)
    {
      Element imageElement = addElement(imagesElement, "image");
      String imageName = "image_" + imageCounter + ".png";
      imageCounter++;
      imageElement.setAttribute("file", imageName);
      try
      {
        ImageIO.write(SwingFXUtils.fromFXImage(landmarks.getImage(), null), "png", new File(imageName));
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
      Element boxElement = addElement(imageElement, "box");
      boxElement.setAttribute("top", Integer.toString(boxTopY.intValue()));
      boxElement.setAttribute("left", Integer.toString(boxLeftX.intValue()));
      boxElement.setAttribute("width", Integer.toString(new Double(2 * radius).intValue()));
      boxElement.setAttribute("height", Integer.toString(new Double(2 * radius).intValue()));
      int partCount = 1;
      for (Position position : landmarks.getPositions())
      {
        Element partElement = addElement(boxElement, "part");
        partElement.setAttribute("name", Integer.toString(partCount));
        partElement.setAttribute("x", Integer.toString(position.getX()));
        partElement.setAttribute("y", Integer.toString(position.getY()));
        partCount++;
      }
    }
  }
    
  public void saveToXML(String xml) 
  {
    try 
    {
      Transformer tr = TransformerFactory.newInstance().newTransformer();
      tr.setOutputProperty(OutputKeys.INDENT, "yes");
      tr.setOutputProperty(OutputKeys.METHOD, "xml");
      tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      try (FileOutputStream stream = new FileOutputStream(xml))
      {
        tr.transform(new DOMSource(dom), new StreamResult(stream));
      }
    } 
    catch (TransformerException | IOException e) 
    {
      throw new RuntimeException(e);
    } 
  }

  private double distance(double x1, double y1, double x2, double y2)
  {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  private Element addElement(Element parent, String elementName)
  {
    Element element = dom.createElement(elementName);
    parent.appendChild(element);
    return element;
  }    

  private void addTextElement(Element parent, String elementName, String elementText)
  {
    Element element = dom.createElement(elementName);
    element.appendChild(dom.createTextNode(elementText));
    parent.appendChild(element);
  }
}
