package textchat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;


public class TextChatConversationController {

	private String localUsername;
	private String remoteUsername;
	private TextChatClient tClient;
    @FXML
    private ResourceBundle resources;

	@FXML
	private TextArea messageBox;

    @FXML
    private URL location;

    @FXML
    private SplitPane godSplitPane;

	@FXML
	private ListView<String> messageList;

	public void addToConvoList(String newMessage){
		messageList.getItems().add(newMessage);	
	}

    @FXML
    void sendMessage(MouseEvent event) {
			tClient.sendMessage(remoteUsername, messageBox.getText());
			messageList.getItems().add(messageBox.getText());
			messageBox.setText("");
    }
	public void initConversationWindow(String localUser, String remoteUser, TextChatClient client){
		localUsername = localUser;
		remoteUsername = remoteUser;
		tClient = client;
		initSlider();
	}
	public void initSlider(){
			/*
			godSplitPane.getDividers().get(0).positionProperty()
					.bind(godSplitPane.heightProperty().divide(4));
			*/

	}
    @FXML
    void initialize() {
        assert godSplitPane != null : "fx:id=\"godSplitPane\" was not injected: check your FXML file 'TextChatConversationController.fxml'.";

    }
}

