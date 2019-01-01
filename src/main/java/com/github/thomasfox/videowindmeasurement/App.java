/*
 * Copyright (c) 2012, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.thomasfox.videowindmeasurement;

import java.io.File;
import java.net.MalformedURLException;

import com.github.thomasfox.videowindmeasurement.fx.AppMenuBar;
import com.github.thomasfox.videowindmeasurement.fx.landmarkfile.ViewerPane;
import com.github.thomasfox.videowindmeasurement.fx.video.PlayerPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application 
{
  private Stage stage;
  
  private PlayerPane playerPane;

  private ViewerPane viewerPane;
  
  private Scene playerScene;

  private Scene viewerScene;
  
  @Override
  public void start(Stage stage) throws MalformedURLException 
  {
    this.stage = stage;
    stage.setTitle("Windspeed rotation detection");
    
    VBox root = new VBox();
    Scene scene = new Scene(root, 1000, 900);
    
    root.getChildren().addAll(createMenuBar());
    
    stage.setScene(scene);
    stage.sizeToScene();
    stage.show();
  }
  
  public void loadVideoFile(File file)
  {
    VBox root = new VBox();
    playerScene = new Scene(root, 1000, 900);
    
    playerPane = new PlayerPane();
    playerPane.loadFile(file);
    
    root.getChildren().addAll(createMenuBar());
    root.getChildren().addAll(playerPane);
    
    viewVideo();
  }
  
  public void viewVideo()
  {
    if (playerScene != null)
    {
      stage.setScene(playerScene);
      stage.sizeToScene();
      stage.show();
    }
  }
  
  public void loadLandmarkFile(File file)
  {
    VBox root = new VBox();
    viewerScene = new Scene(root, 1000, 900);
    
    viewerPane = new ViewerPane(viewerScene);
    viewerPane.loadFile(file);
    
    root.getChildren().addAll(createMenuBar());
    root.getChildren().addAll(viewerPane);
    
    viewLandmarks();
  }
  
  public void viewLandmarks()
  {
    if (viewerScene != null)
    {
      stage.setScene(viewerScene);
      stage.sizeToScene();
      stage.show();
    }
  }
  
  public static void main(String[] args) 
  {
    launch(args);
  }
  
  public AppMenuBar createMenuBar()
  {
    return new AppMenuBar(stage, this::loadVideoFile, this::loadLandmarkFile, this::viewVideo, this::viewLandmarks);
  }
}
