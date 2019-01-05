package com.github.thomasfox.videowindmeasurement.fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphicsUtil
{
  public static void drawCross(GraphicsContext graphicsContext, double x, double y)
  {
    graphicsContext.strokeLine(x - 20, y, x + 20, y);
    graphicsContext.strokeLine(x, y - 20, x, y + 20);
  }

  public static double getScale(double imageWidth, double imageHeight, double boundingBoxWidth, double boundingBoxHeight)
  {
    double scale = Math.min(
        boundingBoxWidth / imageWidth, 
        boundingBoxHeight / imageHeight);
    return scale;
  }
  
  public static double getAngle(double x, double y)
  {
    double angle;
    if (Math.abs(x) > Math.abs(y))
    {
      angle = Math.atan(y/x);
      if (x > 0)
      {
        return Math.PI/2  + angle;
      }
      else
      {
        return 3 * Math.PI / 2 + angle;
      }
    }
    else
    {
      angle = Math.atan(x/y);
      if (y > 0)
      {
        return Math.PI - angle;
      }
      else if (x >= 0)
      {
        return -angle;
      }
      else
      {
        return 2 * Math.PI - angle;
      }
    }
  }
  
  public static Color getColor(int index, int modulus)
  {
    return getColor(index % modulus);
  }
  
  public static Color getColor(int index)
  {
    if (index == 0)
    {
      return Color.BLACK;
    }
    if (index == 1)
    {
      return Color.WHITE;
    }
    if (index == 2)
    {
      return Color.RED;
    }
    return Color.YELLOW;
  }

}
