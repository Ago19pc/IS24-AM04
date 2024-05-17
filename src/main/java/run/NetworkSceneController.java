package run;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class NetworkSceneController extends SceneController{
    public RadioButton rmi_radio_button, socket_radio_button;
    public Button next_button;

    @FXML
    public void setRMI(){
        rmi_radio_button.setSelected(true);
        socket_radio_button.setSelected(false);
        next_button.setDisable(false);
    }

    @FXML
    public void setSocket(){
        rmi_radio_button.setSelected(false);
        socket_radio_button.setSelected(true);
        next_button.setDisable(false);
    }

    @FXML
    public void select_connection_method(){
        if(rmi_radio_button.isSelected()){
            System.out.println("RMI selected");
        }else{
            System.out.println("Socket selected");
        }
        stage.setScene(sceneMap.get(SceneName.JOIN));
    }

}
