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
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main extends Application {

    Scene FirstScene;
    Scene SecondScene;
    File defaultInputFile = new File("D:\\ASU\\3rd Electrical\\Second Term\\Data Structures\\Project\\data\\data-sample.xml");
    File inputFile = defaultInputFile;
    File defaultOutputFile = new File ("D:\\ASU\\3rd Electrical\\Second Term\\Data Structures\\Project\\data\\blabla.xml");
    File outputFile = defaultOutputFile;
    TextArea inputTextArea = new TextArea();
    TextArea outputTextArea = new TextArea();
    BufferedReader dataRead = null;
    BufferedWriter dataWrite = null;
    Tree t;
    TST[] asci = new TST[256];
    long lines = 0;


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
        //Add decompressing
        choice.getItems().addAll("Check Consistency", "Formatting", "Converting to JSON", "Minifying", "Compressing", "De-compressing");
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
        ApplyButton.setOnAction(e->{
            try {
                ApplyButtonClicked(primaryStage, choice, inputFileName, outputFileName);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

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

    public void ApplyButtonClicked(Stage primaryStage, ComboBox <String> choice, TextField inputFileName, TextField outputFileName) throws IOException {
        if (inputFileName == null)
            inputFile = defaultInputFile;
        else {
            t = new Tree();
            try {
                dataRead = new BufferedReader(
                        new FileReader(inputFile));
                System.out.println("entered try in apply");
                lines = t.createTree(dataRead);
                System.out.println("tree created");
            } catch (Exception ex) {
                return;
            }
        }
        if (outputFileName == null)
            outputFile = defaultOutputFile;

        ////////call the function that changes in the files, you don't need to pass any parameters since all the files are in global variables
        if (choice.getValue().compareTo("Check Consistency") == 0){}
        else if (choice.getValue().compareTo("Formatting") == 0)
        {
            try {
                dataWrite = new BufferedWriter(
                        new FileWriter(outputFile));
                t.format(dataWrite);
            }catch(Exception ex)
            {
                return;
            }
        }
        else if (choice.getValue().compareTo("Converting to JSON") == 0){}
        else if (choice.getValue().compareTo("Minifying") == 0)
        {
            try {
                dataWrite = new BufferedWriter(new FileWriter(defaultOutputFile));
                t.minify(dataWrite);
            }catch(Exception ex)
            {
                return;
            }
        }
        else if (choice.getValue().compareTo("Compressing") == 0){System.out.println("entered else if");
            compress();}
        else if (choice.getValue().compareTo("De-compressing") == 0){decompress();}

        System.out.println("returned from else if\n " + lines);
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
        if (lines < 3000 && lines != -1) {
            while (true) {
                try {
                    if (((line = br1.readLine()) == null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sb1.append(line);
                sb1.append(System.lineSeparator());
            }
        }
        else{
            for(int i = 0; i < 3000 ; i++) {
                try {
                    if (((line = br1.readLine()) == null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sb1.append(line);
                sb1.append(System.lineSeparator());
            }
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

        if (lines < 3000 && lines != -1) {
            while (true) {
                try {
                    if (((line2 = br2.readLine()) == null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sb2.append(line2);
                sb2.append(System.lineSeparator());
            }
        }
        else {
            for(int i = 0; i < 3000 ; i++) {
                try {
                    if (((line2 = br2.readLine()) == null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sb2.append(line2);
                sb2.append(System.lineSeparator());
            }
        }
        outputTextArea.setText(sb2.toString());


        primaryStage.setScene(SecondScene);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }




    public void compress() throws IOException {

        //array of Ascii Characters
        for (int i = 0; i < 256; i++)
        {
            TSTNode char_root = new TSTNode((char) i);
            char_root.setCode(i);
            asci[i] = new TST(char_root);
        }

        //reading from and writing to
        FileOutputStream out = null;
        int code = 256;
        TSTNode [] result = new TSTNode[1];
        String s = "";
        int longest_string = 0;

        try {
            out = new FileOutputStream(outputFile.getAbsolutePath());

            int x, y, previous=0;
            boolean lsb = true;
            boolean end = false;
            boolean firstline = true;
            s = "";
            String s2;
            byte[] buf2 = new byte[2];
            dataRead = new BufferedReader(new FileReader(inputFile));
            try {
                while (true) {
                    if((s2 = dataRead.readLine()) == null)
                    {
                        end = true;
                    }
                    else if (firstline)
                    {
                        s = s2;
                        firstline = false;
                    }
                    else
                    {
                        s = s.concat("\r\n" +s2);
                    }
                    while (s.length() > 20 || (end && s.length() > 0)) {
                        longest_string = asci[(int) s.charAt(0)].search(asci[(int) s.charAt(0)].getTSTRoot(), result, s, 0, 0);
                        x = result[0].getCode();
                        if (s.length() > longest_string){
                            if(code < 4096)
                            {
                                asci[(int) s.charAt(0)].insert(result[0], s.charAt(longest_string), true, code);
                                code++;
                            }
                            s = s.substring(longest_string);
                        }
                        else s = "";

                        if (lsb) {
                            buf2[0] = (byte) (x >> 4);
                            previous = x << 4;
                            out.write(buf2, 0, 1);
                            lsb = false;
                        } else {
                            y = x >> 8;
                            y &= 0x0F;
                            previous |= y;
                            buf2[0] = (byte) previous;
                            buf2[1] = (byte) (x);
                            out.write(buf2, 0, 2);
                            lsb = true;
                        }
                    }
                    if (end)
                        break;

                }
                if (lsb == false) {
                    buf2[0] = (byte) previous;
                    out.write(buf2, 0, 1);
                }
                dataRead.close();
            }
            catch (IOException e){
                return;
            }
        }finally {
            if (out != null) {
                out.close();
            }
        }

    }



    public void decompress()
    {
        FileInputStream in = null;
        int n = 50;
        ArrayList<HTElement>[] hash = new ArrayList[n];
        int key = 0;
        for (int i = 0; i < n; i++) {
            hash[i] = new ArrayList<HTElement>();
        }
        try {
            BufferedWriter dataWrite = new BufferedWriter(
                    new FileWriter(outputFile));
            in = new FileInputStream(inputFile.getAbsolutePath());
            int x, previous = 0, j;
            boolean lsb, case1;

            byte[] buf2 = new byte[768];
            int num;


            int code = 256;
            int databytes = 0;
            String lastText = "";
            String data;
            boolean first_decoded = true;
            boolean writeflag = false;
            try {
                while ((num = in.read(buf2)) != -1) {
                    j = 0;
                    lsb = true;
                    case1 = true;
                    while (j < num) {
                        x = (int) (buf2[j]) & 0x0ff;
                        if (x < 0)
                        {
                            x = x + 256;
                        }
                        if (case1) {
                            if (lsb == true) {
                                previous = x << 4 & 0x0fff;
                                lsb = false;
                            } else {
                                previous = (previous | (x >> 4) )& 0x0fff;
                                databytes = previous & 0x0fff;
                                writeflag = true;

                                previous = (x << 8)%4096;;
                                case1 = false;
                                lsb = true;
                            }
                        } else {
                            previous =(previous | x) & 0x000000000000000000000000fff;
                            databytes = previous & 0x0fff;
                            writeflag = true;
                            case1 = true;
                        }
                        if (writeflag == true) {
                            if (databytes == code) {
                                lastText = lastText + lastText.charAt(0);
                                dataWrite.write(lastText);
                                key = (code - 256) % n;
                                HTElement listElement = new HTElement(code, lastText);
                                hash[key].add(listElement);


                            } else {
                                if (databytes > 255) {
                                    key = (databytes - 256) % n;
                                    data = "";
                                    int ksize = hash[key].size();
                                    for (int k = 0; k < ksize; k++) {
                                        if (hash[key].get(k).getCode() == databytes) {
                                            data = hash[key].get(k).getText();
                                            break;
                                        }
                                    }
                                } else {
                                    data = Character.toString((char) databytes);
                                }

                                dataWrite.write(data);
                                if (first_decoded == true)
                                {
                                    lastText = data;
                                    first_decoded = false;
                                    j++;
                                    writeflag = false;
                                    continue;
                                }
                                if (code < 4096) {
                                    lastText = lastText + data.charAt(0);
                                    key = (code - 256) % n;
                                    HTElement listElement = new HTElement(code, lastText);
                                    hash[key].add(listElement);
                                    lastText = data;
                                }


                            }
                            code++;
                            writeflag = false;
                        }
                        j++;
                    }

                }
                //System.out.println("Done");
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            dataWrite.close();
        } catch (Exception ex) {
            return;
        }
    }
}



