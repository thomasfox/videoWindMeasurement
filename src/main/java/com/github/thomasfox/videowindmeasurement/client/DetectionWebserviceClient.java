package com.github.thomasfox.videowindmeasurement.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.thomasfox.videowindmeasurement.json.DetectedImageList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


public class DetectionWebserviceClient
{
  private static ObjectMapper objectMapper = new ObjectMapper();
  
  private static final String BASE_URL = "http://localhost:50000";
  
  public static DetectedImageList detectInImage(Image image)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
      ImageIO.write(bufferedImage, "png", baos);
      
      URL url = new URL(BASE_URL + "/detect/image");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestProperty("Content-Length", String.valueOf(baos.size()));
      connection.setRequestProperty("Content-Type", "image/png");
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);
      OutputStream out = connection.getOutputStream();
      IOUtils.write(baos.toByteArray(), out);
      DetectedImageList imageList = objectMapper.readValue(connection.getInputStream(), DetectedImageList.class);
      return imageList;
    }
    catch (Exception e) 
    {
      throw new RuntimeException(e);
    }
  }
  
  public static void trainDetector(File zipfile)
  {
    System.out.println("file length : " + zipfile.length());
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try (FileInputStream inputStream = FileUtils.openInputStream(zipfile))
      {
        IOUtils.copy(inputStream, baos);
        baos.flush();
      }
      System.out.println("byte array length : " + baos.size());
 
      URL url = new URL(BASE_URL + "/train");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestProperty("Content-Length", String.valueOf(baos.size()));
      connection.setRequestProperty("Content-Type", "application/zip");
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);

      OutputStream out = connection.getOutputStream();
      try (FileInputStream inputStream = FileUtils.openInputStream(zipfile))
      {
        IOUtils.write(baos.toByteArray(), out);
        out.flush();
        out.close();
      }
      String result = IOUtils.toString(connection.getInputStream(), StandardCharsets.ISO_8859_1);
      System.out.println(result);
    }
    catch (Exception e) 
    {
      throw new RuntimeException(e);
    }
  }

}
