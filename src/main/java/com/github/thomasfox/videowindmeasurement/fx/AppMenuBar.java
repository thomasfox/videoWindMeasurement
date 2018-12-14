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
  public AppMenuBar(final Stage stage, Consumer<File> onFileLoaded)
  {
    Menu menuFile = new Menu("File");
    getMenus().add(menuFile);
    
    MenuItem load = new MenuItem("Load");
    load.setOnAction(new OnLoadActionHandler(stage, onFileLoaded));

    menuFile.getItems().addAll(load);
  }
  
  private static class OnLoadActionHandler implements EventHandler<ActionEvent>
  {
    private final FileChooser fileChooser = new FileChooser();

    private final Stage stage;
    
    private final Consumer<File> onFileLoaded;

    public OnLoadActionHandler(Stage stage, Consumer<File> onFileLoaded)
    {
      this.stage = stage;
      this.onFileLoaded = onFileLoaded;
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
