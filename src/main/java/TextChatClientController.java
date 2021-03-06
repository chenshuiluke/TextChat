package textchat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.Node;
import org.controlsfx.control.Notifications;
public class TextChatClientController {
	private HashMap<String, TextChatConversationController> mapOfConversationWindows;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<ClientSerialized> clientList;

	private TextChatClient tClient;
	private Client associatedClient;
	public void initClient(String port, String ip, String username){
		associatedClient = new Client(ip, username);
		tClient = new TextChatClient(Integer.valueOf(port), ip, username, this);
		clientList.setItems(tClient.getObservableOtherClientsList());
	}

	
	TextChatConversationController openChatWindow(String remoteUsername){
		try{
			URL url = getClass().getClassLoader().getResource("TextChatConversationController.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(url);     

			Parent root = (Parent)fxmlLoader.load();          
			TextChatConversationController controller = (TextChatConversationController)fxmlLoader.getController();
			if(controller == null){
				System.out.println("Controller is null!");
			}
		//	controller.initPortAndIP(portTextField.getText(), NetworkUtilities.getExternalIp() + " / " + NetworkUtilities.getInternalIp());

			controller.initConversationWindow(associatedClient.getUsername(), remoteUsername, tClient);
			Scene scene = new Scene(root); 
			Stage stage = new Stage();
			stage.setTitle("Conversation with " + remoteUsername);
			stage.setScene(scene);    

			stage.show();

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			      public void handle(WindowEvent we) {
			          controller.isClosed = true;
			      }
			  }); 


			return controller;
		}
		catch(IOException exc){
			exc.printStackTrace();
			return null;
		}

	}
    @FXML
    void openChatWindowOnClick(MouseEvent event) {
		ClientSerialized selected = clientList.getSelectionModel().getSelectedItem();
		if(selected != null){
			String username = selected.getUsername();
			if(tClient.isWindowInMap(username)){
				TextChatConversationController controller = tClient.getWindowController(username);
				controller.getWindow().requestFocus();

			}
			else{
				tClient.appendWindowToMap(username, openChatWindow(selected.getUsername()));
			}
			
			clientList.getSelectionModel().clearSelection();

		}
    }


    @FXML
    void initialize() {
        assert clientList != null : "fx:id=\"clientList\" was not injected: check your FXML file 'TextChatClientController.fxml'.";
		clientList.setCellFactory(new Callback<ListView<ClientSerialized>, ListCell<ClientSerialized>>() {
            @Override
            public ListCell<ClientSerialized> call(ListView<ClientSerialized> param) {
                ListCell<ClientSerialized> cell = new ListCell<ClientSerialized>() {
                    @Override
                    protected void updateItem(ClientSerialized item, boolean empty) {
                        super.updateItem(item, empty);
			if(item != null){
				Client temp = item.getOriginal();
				if(!associatedClient.equals(temp)){
					Platform.runLater(new Runnable(){
						@Override public void run(){
							setText(item.getUsername());
						}
						
					});
					
				}
				else{
					return;
				}
			}
                        else {
                            return;
                        }
                    }
                };
                return cell;
            }
        });

    }
}

