package com.github.thomasfox.videowindmeasurement.fx;


import java.io.File;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AppMenuBar extends MenuBar
{
  public AppMenuBar(final Stage stage, Consumer<File> onVideoFileLoaded, Consumer<File> onLandmarkFileLoaded)
  {
    Menu menuFile = new Menu("File");
    getMenus().add(menuFile);
    
    MenuItem loadVideo = new MenuItem("Load Video");
    loadVideo.setOnAction(new OnLoadActionHandler(
        stage, 
        "Load Video", 
        new FileChooser.ExtensionFilter("Video Files", "*.mp4"),
        onVideoFileLoaded));
    menuFile.getItems().addAll(loadVideo);

    MenuItem loadLandmarks = new MenuItem("Load Landmarks");
    loadLandmarks.setOnAction(new OnLoadActionHandler(
        stage, 
        "Load Landmarks", 
        new FileChooser.ExtensionFilter("Landmark Files", "*.xml"),
        onLandmarkFileLoaded));
    menuFile.getItems().addAll(loadLandmarks);
  }
  
  private static class OnLoadActionHandler implements EventHandler<ActionEvent>
  {
    private final FileChooser fileChooser = new FileChooser();

    private final Stage stage;
    
    private final Consumer<File> onFileLoaded;

    public OnLoadActionHandler(
        Stage stage, 
        String fileChooserTitle, 
        FileChooser.ExtensionFilter extensionFilter, 
        Consumer<File> onFileLoaded)
    {
      this.stage = stage;
      this.onFileLoaded = onFileLoaded;
      this.fileChooser.setTitle(fileChooserTitle);
      this.fileChooser.getExtensionFilters().add(extensionFilter);
    }
    
    public void handle(ActionEvent t) 
    {
      File file = fileChooser.showOpenDialog(stage);
      if (file != null) 
      {
        onFileLoaded.accept(file);
      }
    }
  }
}
