package Airhockey.Main;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class Chatbox {

    private ArrayList<ChatboxLine> lines;

    ListView listView;
    TextField textField;
    BorderPane borderPane;

    public Chatbox() {
        lines = new ArrayList<>();
    }

    public ArrayList<ChatboxLine> getLines() {
        return lines;
    }

    public void writeLine(ChatboxLine chatboxLine) {
        lines.add(chatboxLine);
    }

    public void display2(Stage primaryStage) {

        Stage s = new Stage();

        listView = new ListView();

        textField = new TextField();
        Button btn = new Button();
        btn.setText("send");

        borderPane = new BorderPane();
        HBox bottom = HBoxBuilder.create().children(textField, btn).build();
        borderPane.setCenter(listView);
        borderPane.setBottom(bottom);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textField.clear();
            }
        });

        s.setScene(new Scene(borderPane, 300, 250));
        s.show();
    }

//    public void display() {
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout());
//        
//        JPanel southPanel = new JPanel();
//        southPanel.setBackground(Color.GRAY);
//        southPanel.setLayout(new GridBagLayout());
//        
//        messageBox = new JTextField(30);
//        messageBox.requestFocusInWindow();
//        
//        sendMessage = new JButton("Send Message");
//        sendMessage.addActionListener(new sendMessageButtonListener());
//        
//        chatBox = new JTextArea();
//        chatBox.setEditable(false);
//        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
//        chatBox.setLineWrap(true);
//        
//        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);
//        
//        GridBagConstraints left = new GridBagConstraints();
//        left.anchor = GridBagConstraints.LINE_START;
//        left.fill = GridBagConstraints.HORIZONTAL;
//        left.weightx = 512.0D;
//        left.weighty = 1.0D;
//        
//        GridBagConstraints right = new GridBagConstraints();
//        right.insets = new Insets(0, 10, 0, 0);
//        right.anchor = GridBagConstraints.LINE_END;
//        right.fill = GridBagConstraints.NONE;
//        right.weightx = 1.0D;
//        right.weighty = 1.0D;
//        
//        southPanel.add(messageBox, left);
//        southPanel.add(sendMessage, right);
//        
//        mainPanel.add(BorderLayout.SOUTH, southPanel);
//        
//        newFrame.add(mainPanel);
//        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        newFrame.setSize(470, 300);
//        newFrame.setVisible(true);
//    }
//    
//    class sendMessageButtonListener implements ActionListener {
//        
//        public void actionPerformed(ActionEvent event) {
//            if (messageBox.getText().length() < 1) {
//                // do nothing
//            } else if (messageBox.getText().equals(".clear")) {
//                chatBox.setText("Cleared all messages\n");
//                messageBox.setText("");
//            } else {
//                chatBox.append("<" + username + ">:  " + messageBox.getText()
//                        + "\n");
//                messageBox.setText("");
//            }
//            messageBox.requestFocusInWindow();
//        }
//    }
//    
//    String username;
//    
//    class enterServerButtonListener implements ActionListener {
//        
//        public void actionPerformed(ActionEvent event) {
//            username = usernameChooser.getText();
//            if (username.length() < 1) {
//                System.out.println("No!");
//            } else {
//                preFrame.setVisible(false);
//                display();
//            }
//        }
//        
//    }
}
