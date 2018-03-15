package edu.tr.mef.comp106.fxquickstart;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class MainPaneController implements Initializable {

    @FXML
    ImageView ivMain;

    @FXML
    ImageView ivCanvas;

    @FXML
    Button btnSelectImage;
    @FXML
    Button btnClear;
    @FXML
    Button btnSave;

    PixelWriter writer;

    int width;
    int height;

    int[][] binaryMatrix;
    int[][] labelMatrix;
    Color selectedColor = Color.WHITE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSelectImage.setOnAction(btnOnClick);
        btnClear.setOnAction(btnclr);
        btnSave.setOnAction(btnforsave);

        ivCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                selectedColor = ivCanvas.getImage().getPixelReader().getColor(x, y);
                System.out.println("Color getted:");
            }
        });

        ivMain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int selectedLabel = labelMatrix[y][x];
                for (int i = 0; i < labelMatrix.length; i++) {
                    for (int j = 0; j < labelMatrix[0].length; j++) {
                        if (labelMatrix[i][j] == selectedLabel) {
                            writer.setColor(j, i, selectedColor);
                        }
                    }
                }
            }
        });

    }

    EventHandler<MouseEvent> setOnMouseOnClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            int a = (int) event.getX();
            int b = (int) event.getY();
        }
    };
    public EventHandler<ActionEvent> btnclr = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {

            for (int i = 0; i < labelMatrix.length; i++) {
                for (int j = 0; j < labelMatrix[0].length; j++) {
                    if (binaryMatrix[i][j] == 1) {
                        writer.setColor(j, i, Color.WHITE);
                    }
                }
            }
        }
    };

    public EventHandler<ActionEvent> btnforsave = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            File outputFile = new File("src/edu/tr/mef/comp106/fxquickstart/images/abc.png");
            BufferedImage bImage = SwingFXUtils.fromFXImage(ivMain.snapshot(null, null), null);
            try {
                ImageIO.write(bImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    };

    public EventHandler<ActionEvent> btnOnClick = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File("src/edu/tr/mef/comp106/fxquickstart/images"));
            chooser.setTitle("Open File");
            File file = chooser.showOpenDialog(new Stage());

            if (file == null) {
                return;
            }

            Image image = new Image(file.toURI().toString());
            width = (int) image.getWidth();
            height = (int) image.getHeight();

            binaryMatrix = new int[height][width];
            labelMatrix = new int[height][width];

            PixelReader pixelReader = image.getPixelReader();

            WritableImage writableImage = new WritableImage(width, height);

            writer = writableImage.getPixelWriter();
            writer.setPixels(0, 0, width, height, pixelReader, 0, 0);

            image = writableImage;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    labelMatrix[i][j] = 0;
                    if (pixelReader.getColor(j, i).equals(Color.BLACK)) {
                        binaryMatrix[i][j] = 0;
                    } else {
                        binaryMatrix[i][j] = 1;
                    }
                }
            }

            for (int i = 0; i < width; i++) {
                binaryMatrix[0][i] = 0;
                binaryMatrix[height - 1][i] = 0;
            }

            for (int i = 0; i < height; i++) {
                binaryMatrix[i][0] = 0;
                binaryMatrix[i][width - 1] = 0;
            }

            int label = 0;

            // 8 connected
           


for(int i = 0; i < height-1; i++){
                for(int j = 0; j < width-1; j++){
                    if(binaryMatrix[i][j] == 1){
                        if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==0){
                            label++;
                            labelMatrix[i][j]=label;
                        }
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==1){
                            labelMatrix[i][j]=labelMatrix[i][j-1];
                        }
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==0){
                            labelMatrix[i][j]=labelMatrix[i-1][j+1];
                        }
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==1){
                            if(labelMatrix[i-1][j+1]==labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j+1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j+1];
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i][j-1]);
                                        }
                        }
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==0){
                            labelMatrix[i][j]=labelMatrix[i-1][j];
                        }
                        
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==1){
                            if(labelMatrix[i-1][j]==labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                                correctLabel(labelMatrix[i][j-1],labelMatrix[i-1][j]);
                            }
                            
                        }
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==0){
                            if(labelMatrix[i-1][j]==labelMatrix[i-1][j+1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j]);
                                        }
                            
                        }
                        //3
                        else if(binaryMatrix[i-1][j-1]==0 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==1){
                             if(labelMatrix[i-1][j] == labelMatrix[i-1][j+1] && labelMatrix[i-1][j+1]== labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j];
                                labelMatrix[i-1][j+1]=labelMatrix[i-1][j];
                                labelMatrix[i][j-1]= labelMatrix[i-1][j];
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j]);
                                correctLabel(labelMatrix[i][j-1],labelMatrix[i-1][j]);
                                        }
                            
                        }
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==0){
                            labelMatrix[i][j]=labelMatrix[i-1][j-1];
                        }
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==1){
                            if(labelMatrix[i-1][j-1]==labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i][j-1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==0){
                            if(labelMatrix[i-1][j-1]==labelMatrix[i-1][j+1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j+1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        //3
                        else if(binaryMatrix[i-1][j-1]==1 && binaryMatrix[i-1][j]==0 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==1){
                            if(labelMatrix[i-1][j-1] == labelMatrix[i-1][j+1] && labelMatrix[i-1][j+1]== labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j+1]=labelMatrix[i-1][j-1];
                                labelMatrix[i][j-1]= labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j-1]);
                                correctLabel(labelMatrix[i][j-1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==0){
                            if(labelMatrix[i-1][j-1]==labelMatrix[i-1][j]){
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i-1][j],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        //3
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==0 && binaryMatrix[i][j-1]==1){
                            if(labelMatrix[i-1][j-1] == labelMatrix[i-1][j] && labelMatrix[i-1][j] == labelMatrix[i][j-1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i][j-1]= labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i-1][j],labelMatrix[i-1][j-1]);
                                correctLabel(labelMatrix[i][j-1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        //3
                        else if(binaryMatrix[i-1][j-1]==1 &&binaryMatrix[i-1][j]==1 && binaryMatrix[i-1][j+1]==1 && binaryMatrix[i][j-1]==0){
                            if(labelMatrix[i-1][j-1] == labelMatrix[i-1][j] && labelMatrix[i-1][j]== labelMatrix[i-1][j+1]){
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j+1]= labelMatrix[i-1][j-1];
                                correctLabel(labelMatrix[i-1][j],labelMatrix[i-1][j-1]);
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                        //4
                        else{
                            if(labelMatrix[i-1][j-1] == labelMatrix[i-1][j] && labelMatrix[i-1][j]== labelMatrix[i-1][j+1] &&labelMatrix[i-1][j+1] == labelMatrix[i][j-1] ){
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                            }
                            else{
                                labelMatrix[i][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j]=labelMatrix[i-1][j-1];
                                labelMatrix[i-1][j+1]= labelMatrix[i-1][j-1];
                                labelMatrix[i][j-1]= labelMatrix[i-1][j-1];

                                correctLabel(labelMatrix[i-1][j],labelMatrix[i-1][j-1]);
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j-1]);
                                correctLabel(labelMatrix[i-1][j+1],labelMatrix[i-1][j-1]);
                                        }
                            
                        }
                    }}
                    }
            //
            //

            ivMain.setImage(image);
            ivMain.setFitHeight(0);
            ivMain.setFitWidth(0);
            System.out.println("" + ivMain);
        }

    };

    private void correctLabel(int label1, int label2) {

        for (int i = 0; i < labelMatrix[0].length; i++) {
            for (int j = 0; j < labelMatrix.length; j++) {
                if (labelMatrix[j][i] == label1) {
                    labelMatrix[j][i] = label2;
                }
            }
        }

    }
}
