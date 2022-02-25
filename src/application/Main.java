package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Main extends Application {
	PrintWriter pw;
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			
			Label labelHost = new Label("Host:");
			TextField textFieldHost = new TextField("localhost");
			Label labelPort = new Label("Port:");
			TextField textFieldPort = new TextField("1234");
			Button buttonConnect = new Button("Connect");
			
			HBox hBox1 = new HBox();hBox1.setSpacing(10);hBox1.setPadding(new Insets(10));
			hBox1.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonConnect);
			hBox1.setBackground(new Background(new BackgroundFill(Color.LAVENDER, null, null)));
			root.setTop(hBox1);
			
			VBox vBox = new VBox();vBox.setSpacing(10);vBox.setPadding(new Insets(10));
			ObservableList<String> listModel = FXCollections.observableArrayList();
			ListView<String> listView = new ListView<String>(listModel);
			vBox.getChildren().add(listView);
			root.setCenter(vBox);
			
			Label labelMessage = new Label("Message:");
			TextField textFieldMessage = new TextField(); textFieldMessage.setPrefSize(400, 30);
			Button buttonSend = new Button("Send!");
			
			HBox hBox2 = new HBox();hBox2.setSpacing(10);hBox2.setPadding(new Insets(10));
			hBox2.getChildren().addAll(labelMessage,textFieldMessage,buttonSend);
			hBox2.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
			root.setBottom(hBox2);
			
			
			Scene scene = new Scene(root,550,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Chat Application");
			primaryStage.show();
			
			// Events
			
			buttonConnect.setOnAction((event)->{
				String host = textFieldHost.getText();
				int port = Integer.parseInt(textFieldPort.getText());
				try {
					Socket socket = new Socket(host,port);
					InputStream is=socket.getInputStream();
					BufferedReader br=new BufferedReader(new InputStreamReader(is));
					pw=new PrintWriter(socket.getOutputStream(),true);
					new Thread(()->{
						while(true) {
							
							try {
								
								String response = br.readLine();
								Platform.runLater(()->{
								listModel.add(response);
								});
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			buttonSend.setOnAction((evt)->{
				String message = textFieldMessage.getText();
				pw.println(message);
				textFieldMessage.clear();
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
