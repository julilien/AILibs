package jaicore.search.gui;

import jaicore.graphvisualizer.SearchVisualizationPanel;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class FXController implements Initializable  {

    //contains the GraphVisualizationPane
    @FXML
    private SwingNode swingNode;
    //slider for replay speed
    @FXML
    private Slider slider;

    //slider as a timeline
    @FXML
    private Slider timeline;
    //noinspection unchecked
    private List<Long> eventTimes;



    private static Recorder rec;
    private Thread controllerThread;

    private long sleepTime;

    private int index;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createSwingContent(swingNode);

        sleepTime = 50;

        //if the slider for replay-speed is released, wait (200 ms - the value of the slider)
        //the slider has a range from 0 to 200
        slider.setOnMouseReleased((MouseEvent event)-> {
            sleepTime = (long) (200 - slider.getValue());
        });

        setTimeline();
        index = 0;


    }

    /**
     * Creates the SearchVisualizationPanel and binds it to the swingNode in the GUI
     * @param swingnode
     */
    private void createSwingContent(SwingNode swingnode){
        if(swingnode.getContent() != null)
            rec.unregisterListener(swingnode.getContent());
        SearchVisualizationPanel panel = new SearchVisualizationPanel();
        rec.registerListener(panel);
        SwingUtilities.invokeLater(()-> swingnode.setContent(panel));
    }

    /**
     * Starts the playback of the events. For this an own thread is created, in order to not freeze the whole GUI.
     * The playback can be stopped by pressing the stop button.
     */
   @FXML
    protected void play(ActionEvent event){
       System.out.println("play");
       int numberOfEvents = rec.getNumberOfEvents();

       // create an own thread for playing the steps, in order to prevent freezing and make the replay stoppable
       Runnable runPlay = () ->{
          try{
              for(int i = index; i < numberOfEvents; i ++){
                  rec.step();
                  TimeUnit.MILLISECONDS.sleep(sleepTime);
                  timeline.setValue(eventTimes.get(i));
              }
          }
          catch (InterruptedException e){}
       };
       controllerThread = new Thread(runPlay);
       controllerThread.start();
   }

    /**
     * Takes a single step, if the step-Button is pressed.
     * @param event
     *      Button press event on the Button step
     */
   @FXML
    protected void step(ActionEvent event){
        rec.step();
        index ++;
        timeline.setValue(eventTimes.get(index));
        System.out.println(index);

   }

    /**
     * Takes one step backewards, if the button is pressed
     * @param event
     *      The event fired, by pressing the back button
     */
   @FXML
    protected void back(ActionEvent event){
        System.out.println("back");
        if(timeline.getValue() == 0)
            System.out.println("I am at the start");
        else
            if(index == 1){
                createSwingContent(swingNode);
                index = 0;
                setTimeline();
                rec.reset();
            }
            else
                rec.back();

   }

    /**
     * Reset the gui
     * @param event
     *      The event fired, by pressing the reset button
     */
   @FXML
    protected void reset(ActionEvent event){
        System.out.println("reset");
        controllerThread.interrupt();
        createSwingContent(swingNode);
        index = 0;
        setTimeline();
        rec.reset();
   }

    /**
     * Stops the current replay. This button does nothing, if the play-Button is not pressed before
     * @param event
     *      The event fired, by pressing the stop button
     */
   @FXML
   protected void stop(ActionEvent event){
        controllerThread.interrupt();
        System.out.println("Stop");
   }

    /**
     * Opens a file chooser, in which the user has to specify a file to store the events.
     * @param event
     *      The event fired by pressing the save button.
     */
   @FXML
   protected void save(ActionEvent event){
       FileChooser chooser = new FileChooser();
       chooser.setTitle("Choose Event-File");
       rec.saveToFile(chooser.showSaveDialog(null));

//       File file = new File("/home/jkoepe/Documents/Test.txt");
//       rec.saveToFile(file);
   }


    @FXML
    protected void load(ActionEvent event){
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("Open Event-File");
    	rec.loadFromFile(chooser.showOpenDialog(null));
//        File file = new File("/home/jkoepe/Documents/Test.txt");
//        rec.loadFromFile(file);
        createSwingContent(swingNode);

        setTimeline();



    }

   public static void setRec(Recorder recorder){
        rec = recorder;
    }

    /**
     * Sets the values of the timeline to match the currently stored events in the recorder.
     */
    private void setTimeline(){
       eventTimes = rec.getEventTimes();
       timeline.setMax(rec.getLastEvent());
       if(!eventTimes.isEmpty())
           timeline.setMajorTickUnit(rec.getLastEvent()>>4);
       timeline.setValue(index);
   }


}
