package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    Scene FirstScene;
    Scene SecondScene;
    File defaultInputFile = new File("D:\\ASU\\3rd Electrical\\Second Term\\Data Structures\\Project\\data\\data-sample.xml");
    File inputFile = defaultInputFile;
    File defaultOutputFile = new File ("D:\\ASU\\3rd Electrical\\Second Term\\Data Structures\\Project\\data\\blabla.xml");
    File outputFile = defaultOutputFile;
    TextArea inputTextArea = new TextArea();
    TextArea outputTextArea = new TextArea();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        //setting title of the stage
        primaryStage.setTitle("XML Editor");

//////////////
// FIRST SCENE
//////////////

        //identifying the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //choosing feature from combobox
        Label choiceLabel = new Label("Chosen Feature:");
        GridPane.setConstraints(choiceLabel, 0,0 );

        ComboBox<String> choice= new ComboBox<>();
        choice.getItems().addAll("Check Consistency", "Formatting", "Converting to JSON", "Minifying", "Compressing");
        choice.setPromptText("Choose one");
        GridPane.setConstraints(choice, 1,0 );

        //choosing input file
        FileChooser filePath = new FileChooser();
        filePath.setTitle("Select File");
        filePath.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json") );


        Label fileLabel = new Label("File Name:");
        GridPane.setConstraints(fileLabel, 0,1 );

        TextField inputFileName = new TextField();
        inputFileName.setText(defaultInputFile.getAbsolutePath());
        inputFileName.setMinWidth(200);
        GridPane.setConstraints(inputFileName, 1,1);

        Button BrowseButton = new Button("Browse");
        BrowseButton.setOnAction(e->{BrowseButtonClicked(primaryStage, filePath, inputFileName);});
        GridPane.setConstraints(BrowseButton, 2,1 );

        //choosing output file
        Label outputLabel = new Label("Save as:");
        outputLabel.setFont(Font.font("",FontWeight.BOLD, FontPosture.REGULAR,14));
        GridPane.setConstraints(outputLabel, 0,3 );

        Label outputFileLabel = new Label("File Name:");
        GridPane.setConstraints(outputFileLabel, 0,4 );

        TextField outputFileName = new TextField();
        outputFileName.setText(defaultOutputFile.getAbsolutePath());
        outputFileName.setMinWidth(200);
        GridPane.setConstraints(outputFileName, 1,4);

        Button SaveButton = new Button("Browse");
        SaveButton.setOnAction(e->{SaveButtonClicked(primaryStage, filePath, outputFileName);});
        GridPane.setConstraints(SaveButton, 2,4);

        grid.getChildren().addAll(choiceLabel,choice, fileLabel,inputFileName, BrowseButton, outputLabel, outputFileLabel, outputFileName, SaveButton);

        //identifying apply button
        Button ApplyButton = new Button("Apply");
        ApplyButton.setOnAction(e->{ApplyButtonClicked(primaryStage, choice, inputFileName, outputFileName); });

        //putting constraints on HBox and VBox
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(ApplyButton);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(grid, hBox);

        //showing first scene
        FirstScene = new Scene (vBox);
        primaryStage.setScene(FirstScene);
        primaryStage.show();


///////////////
// SECOND SCENE
///////////////

        //identifying the grid
        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        grid2.setVgap(8);
        grid2.setHgap(10);

        //input file
        Label LabelInputFile = new Label("Input File:");
        LabelInputFile.setFont(Font.font("",FontWeight.BOLD, FontPosture.REGULAR,14));
        GridPane.setConstraints(LabelInputFile, 0,0 );

        inputTextArea.setPrefHeight(600);
        inputTextArea.setPrefWidth(650);
        GridPane.setConstraints(inputTextArea, 0,1 );

        //output file
        Label LabelOutputFile = new Label("Output File:");
        LabelOutputFile.setFont(Font.font("",FontWeight.BOLD, FontPosture.REGULAR,14));
        GridPane.setConstraints(LabelOutputFile, 1,0 );

        outputTextArea.setPrefHeight(600);
        outputTextArea.setPrefWidth(650);
        GridPane.setConstraints(outputTextArea, 1,1 );


        //Retry button
        Button RetryButton = new Button("Retry");
        RetryButton.setOnAction(e->{ primaryStage.setScene(FirstScene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2); });

        //Exit button
        Button ExitButton = new Button("Exit");
        ExitButton.setOnAction(e->{Platform.exit();});

        grid2.getChildren().addAll(LabelInputFile, inputTextArea, LabelOutputFile, outputTextArea, RetryButton, ExitButton);

        //putting constraints on HBox and VBox
        HBox hBox2 = new HBox();
        hBox2.setPadding(new Insets(10,10,10,10));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(RetryButton, ExitButton);

        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(grid2, hBox2);

        SecondScene = new Scene (vBox2);

    }

    public void BrowseButtonClicked(Stage primaryStage, FileChooser filePath, TextField inputFileName){
        inputFile = filePath.showOpenDialog(primaryStage);

        if (inputFile != null) {
            defaultInputFile = inputFile;
        }
        else{
            inputFile = defaultInputFile;
        }
        inputFileName.setText(inputFile.getAbsolutePath());
    }

    public void SaveButtonClicked(Stage primaryStage, FileChooser filePath, TextField outputFileName){
        outputFile = filePath.showSaveDialog(primaryStage);

        if (outputFile != null) {
            defaultOutputFile = outputFile;
        }
        else{
            outputFile = defaultOutputFile;
        }
        outputFileName.setText(outputFile.getAbsolutePath());
    }

    public void ApplyButtonClicked(Stage primaryStage, ComboBox <String> choice, TextField inputFileName, TextField outputFileName){
        if (inputFileName == null)
            inputFile = defaultInputFile;
        if (outputFileName == null)
            outputFile = defaultOutputFile;

        ////////call the function that changes in the files, you don't need to pass any parameters since all the files are in global variables
        if (choice.getValue().compareTo("Check Consistency") == 0){}
        else if (choice.getValue().compareTo("Formatting") == 0){}
        else if (choice.getValue().compareTo("Converting to JSON") == 0){}
        else if (choice.getValue().compareTo("Minifying") == 0){}
        else if (choice.getValue().compareTo("Compressing") == 0){}

        //reading input file to text area
        String fileName = inputFile.getAbsolutePath();
        BufferedReader br1 = null;
        try {
            br1 = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb1 = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if (!((line = br1.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            sb1.append(line);
            sb1.append(System.lineSeparator());
        }
        inputTextArea.setText(sb1.toString());

        //reading output file to text area
        //String fileName2 = outputFile.getAbsolutePath(); //this should be used instead when we are really working
        String fileName2 = defaultOutputFile.getAbsolutePath();
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader(fileName2, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb2 = new StringBuilder();
        String line2 = null;
        while (true) {
            try {
                if (!((line2 = br2.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            sb2.append(line2);
            sb2.append(System.lineSeparator());
        }
        outputTextArea.setText(sb2.toString());


        primaryStage.setScene(SecondScene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        }

    }
