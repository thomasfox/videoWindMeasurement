package com.github.thomasfox.videowindmeasurement.fx;

import javafx.scene.canvas.GraphicsContext;

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
}
