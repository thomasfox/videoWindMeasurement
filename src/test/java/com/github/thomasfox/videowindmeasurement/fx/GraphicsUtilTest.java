package com.github.thomasfox.videowindmeasurement.fx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import org.junit.Test;

public class GraphicsUtilTest
{
  @Test
  public void getAngle_0()
  {
    assertThat(GraphicsUtil.getAngle(0d, -10d)).isCloseTo(0, offset(0.00001d));
  }
  
  @Test
  public void getAngle_30()
  {
    assertThat(GraphicsUtil.getAngle(Math.sqrt(3), -3)).isCloseTo(Math.PI/6, offset(0.00001d));
  }

  @Test
  public void getAngle_45()
  {
    assertThat(GraphicsUtil.getAngle(5d, -5d)).isCloseTo(Math.PI/4, offset(0.00001d));
  }

  @Test
  public void getAngle_60()
  {
    assertThat(GraphicsUtil.getAngle(3d, -Math.sqrt(3))).isCloseTo(2 * Math.PI/6, offset(0.00001d));
  }

  @Test
  public void getAngle_90()
  {
    assertThat(GraphicsUtil.getAngle(10d, 0d)).isCloseTo(Math.PI/2, offset(0.00001d));
  }
  
  @Test
  public void getAngle_120()
  {
    assertThat(GraphicsUtil.getAngle(3d, Math.sqrt(3))).isCloseTo(4 * Math.PI/6, offset(0.00001d));
  }

  @Test
  public void getAngle_135()
  {
    assertThat(GraphicsUtil.getAngle(5d, 5d)).isCloseTo(3 * Math.PI/4, offset(0.00001d));
  }
  
  @Test
  public void getAngle_150()
  {
    assertThat(GraphicsUtil.getAngle(Math.sqrt(3), 3)).isCloseTo(5 * Math.PI/6, offset(0.00001d));
  }

  @Test
  public void getAngle_180()
  {
    assertThat(GraphicsUtil.getAngle(0d, 10d)).isCloseTo(Math.PI, offset(0.00001d));
  }
  
  @Test
  public void getAngle_210()
  {
    assertThat(GraphicsUtil.getAngle(-Math.sqrt(3), 3)).isCloseTo(7 * Math.PI/6, offset(0.00001d));
  }
  
  @Test
  public void getAngle_270()
  {
    assertThat(GraphicsUtil.getAngle(-10d, 0d)).isCloseTo(3 * Math.PI/2, offset(0.00001d));
  }
  
  @Test
  public void getAngle_330()
  {
    assertThat(GraphicsUtil.getAngle(-Math.sqrt(3), -3)).isCloseTo(11 * Math.PI/6, offset(0.00001d));
  }
}
