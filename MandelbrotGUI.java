// Author: Mehrad Hajati
// Date: 17/04/2022
// This class creates a GUI that displays the MandelBrot set and allows the user to move around the complex plane and to zoom in and out
// The pictures need to be placed in a folder called "buttonPics" which is placed in the same directory as the program for the buttons to have picutres on them


import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.geometry.*;
import javafx.event.*;

public class MandelbrotGUI extends Application{

    // Class constants
    // most of these constants were chosen after some experimentation
    private static final int BUTTON_WIDTH = 85;
    private static final int BUTTON_HEIGHT = 30;
    private static final int BUTTON_TOP_OFFSET = 200;
    private static final int PIXEL_GRID_WIDTH = 600;
    private static final int PIXEL_GRID_HEIGHT = 600;
    private static final int MAX_ITERATIONS = 100;
    private static final int RADIUS = 2;
    private static final double TOP_LEFT_CORNER_IMAG = 1.8;
    private static final double TOP_LEFT_CORNER_REAL = -2.3;
    private static final double DEFAULT_SIDE_LENGTH = 3.6;
    private static final int INTERPOLATE_CONSTANT = 13;
    private static final double MOVE_AMOUNT = 0.2;
    private static final double ZOOM_AMOUNT = 0.2;

    // Instance variables
    private Scene scene;
    private Stage stage;
    private Complex corner;
    private double sideLength;
    private BorderPane bPane;
    private Button moveUp;
    private Button moveDown;
    private Button moveLeft;
    private Button moveRight;
    private Button zoomIn;
    private Button zoomOut;
    private Button initial;
    private Button exit;
    private ToolBar leftToolBar;
    private ToolBar rightToolBar;
    private Label bottomLabel;
    private ImageView iView;
    private PixelWriter pixelWriter;

    // Start method
    public void start(Stage stage) {
        this.stage = stage;
        setUpGUI();
        scene = new Scene(bPane, Color.GOLDENROD);
        stage.setScene(scene);
        stage.setTitle("Mandlebrot Fractal GUI");
        setUpEventHandlers();
        stage.show();
    }
    

    private void setUpGUI() {

        // creating the borderpane
        bPane = new BorderPane();

        // creating two spacers for our toolbars
        Region leftSpacer = new Region();
        leftSpacer.setPrefHeight(BUTTON_TOP_OFFSET);
        Region rightSpacer = new Region();
        rightSpacer.setPrefHeight(BUTTON_TOP_OFFSET);

        // creating images for the up button
        Image upArrowImage = new Image("buttonPics/uparrow.png");
        ImageView upArrowView = new ImageView(upArrowImage); 
        upArrowView.setFitWidth(BUTTON_WIDTH);
        upArrowView.setFitHeight(BUTTON_HEIGHT);
        upArrowView.setPreserveRatio(true);

        //creating images for the down button
        Image downArrowImage = new Image("buttonPics/downarrow.jpg");
        ImageView downArrowView = new ImageView(downArrowImage); 
        downArrowView.setFitWidth(BUTTON_WIDTH);
        downArrowView.setFitHeight(BUTTON_HEIGHT);
        downArrowView.setPreserveRatio(true);

        // creating images for the left button
        Image leftArrowImage = new Image("buttonPics/leftarrow.png");
        ImageView leftArrowView = new ImageView(leftArrowImage); 
        leftArrowView.setFitWidth(BUTTON_WIDTH);
        leftArrowView.setFitHeight(BUTTON_HEIGHT);
        leftArrowView.setPreserveRatio(true);

        // creating images for the right button
        Image rightArrowImage = new Image("buttonPics/rightarrow.png");
        ImageView rightArrowView = new ImageView(rightArrowImage); 
        rightArrowView.setFitWidth(BUTTON_WIDTH);
        rightArrowView.setFitHeight(BUTTON_HEIGHT);
        rightArrowView.setPreserveRatio(true);

        // creating images for the zoom in button
        Image zoomInImage = new Image("buttonPics/zoomin.png");
        ImageView zoomInView = new ImageView(zoomInImage); 
        zoomInView.setFitWidth(BUTTON_WIDTH);
        zoomInView.setFitHeight(BUTTON_HEIGHT);
        zoomInView.setPreserveRatio(true);

        // creating images for the zoom out button
        Image zoomOutImage = new Image("buttonPics/zoomout.png");
        ImageView zoomOutView = new ImageView(zoomOutImage); 
        zoomOutView.setFitWidth(BUTTON_WIDTH);
        zoomOutView.setFitHeight(BUTTON_HEIGHT);
        zoomOutView.setPreserveRatio(true);

        // creating images for the reset button
        Image resetImage = new Image("buttonPics/reset.png");
        ImageView resetView = new ImageView(resetImage); 
        resetView.setFitWidth(BUTTON_WIDTH);
        resetView.setFitHeight(BUTTON_HEIGHT);
        resetView.setPreserveRatio(true);

        // creating images for the reset button
        Image exitImage = new Image("buttonPics/exit.png");
        ImageView exitView = new ImageView(exitImage); 
        exitView.setFitWidth(BUTTON_WIDTH);
        exitView.setFitHeight(BUTTON_HEIGHT);
        exitView.setPreserveRatio(true);

        // creating buttons
        moveUp = new Button("" , upArrowView);
        moveDown = new Button("", downArrowView);
        moveLeft = new Button("", leftArrowView);
        moveRight = new Button("", rightArrowView);
		zoomIn = new Button("", zoomInView);
		zoomOut = new Button("", zoomOutView);
		initial = new Button("", resetView);
        exit = new Button("", exitView);

        // setting button widths
        moveDown.setPrefWidth(BUTTON_WIDTH);
        moveLeft.setPrefWidth(BUTTON_WIDTH);
        moveRight.setPrefWidth(BUTTON_WIDTH);
        moveUp.setPrefWidth(BUTTON_WIDTH);
        zoomIn.setPrefWidth(BUTTON_WIDTH);
        zoomOut.setPrefWidth(BUTTON_WIDTH);
        initial.setPrefWidth(BUTTON_WIDTH);
        exit.setPrefWidth(BUTTON_WIDTH);

        // creating our two toolbars
		leftToolBar = new ToolBar(leftSpacer, zoomIn, zoomOut, initial, exit);
        rightToolBar = new ToolBar(rightSpacer, moveUp, moveDown, moveRight, moveLeft);
        leftToolBar.setOrientation(Orientation.VERTICAL);
        rightToolBar.setOrientation(Orientation.VERTICAL);

        // placing our toolbars in the border pane
        bPane.setLeft(leftToolBar);
        bPane.setRight(rightToolBar);

        // creating a label for the GUI
        bottomLabel = new Label(" \u00A9 Mehrad Hajati");
        bPane.setBottom(bottomLabel);

        // creating a writableImage
        WritableImage wImage = new WritableImage(PIXEL_GRID_WIDTH, PIXEL_GRID_HEIGHT);
        iView = new ImageView(wImage);
        bPane.setCenter(iView);
        pixelWriter = wImage.getPixelWriter();

        // Creating the initial/default picture of the mandelbrot with the constants
        createMandleBrot(TOP_LEFT_CORNER_REAL, TOP_LEFT_CORNER_IMAG, pixelWriter, DEFAULT_SIDE_LENGTH);
        this.corner = new Complex(TOP_LEFT_CORNER_REAL, TOP_LEFT_CORNER_IMAG);
        this.sideLength = DEFAULT_SIDE_LENGTH;
    }
        
    
    private void setUpEventHandlers() {

        // for the exit button
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        // for the reset button
        initial.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event){
                sideLength = DEFAULT_SIDE_LENGTH;
                createMandleBrot(TOP_LEFT_CORNER_REAL, TOP_LEFT_CORNER_IMAG, pixelWriter, sideLength);
                corner = new Complex(TOP_LEFT_CORNER_REAL, TOP_LEFT_CORNER_IMAG);
            }
        });

        // for the moveDown button to move the set 20 percent lower
        moveDown.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                double constant = corner.getReal();
                double change = corner.getImag() - (sideLength * MOVE_AMOUNT);
                createMandleBrot(constant, change, pixelWriter, sideLength);
                corner = new Complex(constant, change);
            }
        });

        // for the moveUp button to move the set 20 percent higher
        moveUp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                double constant = corner.getReal();
                double change = corner.getImag() + (sideLength * MOVE_AMOUNT);
                createMandleBrot(constant, change, pixelWriter, sideLength);
                corner = new Complex(constant, change);
            }
        });

        // for the moveLeft button to move the set 20 percent to the left
        moveLeft.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                double constant = corner.getImag();
                double change = corner.getReal() - (sideLength * MOVE_AMOUNT);
                createMandleBrot(change, constant, pixelWriter, sideLength);
                corner = new Complex(change, constant);
            }
        });

        // for the moveRight button to move the set 20 percent to the right
        moveRight.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                double constant = corner.getImag();
                double change = corner.getReal() + (sideLength * MOVE_AMOUNT);
                createMandleBrot(change, constant, pixelWriter, sideLength);
                corner = new Complex(change, constant);
            }
        });

        // for the zoomIn button, it reduces the side lengths by 20 percent
        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                sideLength = sideLength * (1-ZOOM_AMOUNT);
                createMandleBrot(corner.getReal(), corner.getImag(), pixelWriter, sideLength);
            }
        });

        // for the zoomOut button, it increases the side lengths by 20 percent
        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                sideLength = sideLength * (1+ZOOM_AMOUNT);
                createMandleBrot(corner.getReal(), corner.getImag(), pixelWriter, sideLength);
            }
        });

        // This method is to give the user the ability to move around using W,A,S,D keys on the keyboard
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event){
                // This switch statement is for the different possible keys pressed on the keyboard
                switch(event.getCode()){
                    // In each of these cases, one of the WASD keys was pressed and we fire the corresponding button
                    case W:
                        moveUp.fire();
                        break;
                    case S:
                        moveDown.fire();
                        break;
                    case D:
                        moveRight.fire();
                        break;
                    case A:
                        moveLeft.fire();
                        break;
                    case ESCAPE:
                        exit.fire();
                        break;
                    default:
                        System.out.println("Press W,A,S,D to move or ESC to exit");
                }
            }
        });
        
    }
    
    public static void main(String[] args) {
        Application.launch(args);
        
    }
    
    // doIterations method
    // this method takes a complex point on the plane and then does the mathematical iterations for the MandelBrot set 
    private static int doIterations(Complex c){
        int m = 0;
        double imag = c.getImag();
        double real = c.getReal();
        Complex z = new Complex(real, imag);
        while(m < MAX_ITERATIONS){
            z.multiply(z);
            z.add(c);
            if(z.modulus() > RADIUS){
                return m;
            }
            m++;
        }
        return MAX_ITERATIONS;
    }

    // pickColor method
    // This method takes in the number of iterations from the doItrations method and then assigns a color to that point in the complex plane
    private static Color pickColor(int numIter){
        if(numIter == MAX_ITERATIONS){
            return Color.BLUE;
        }
        else{
            return Color.GOLDENROD.interpolate(Color.CYAN, numIter/INTERPOLATE_CONSTANT);
        }
    }


    // this method is to create the mandlebrot set with a given top left corner as starting position
    // also this method needs the pixel writor and the side length of the 
    private static void createMandleBrot(double cornerReal, double cornerImag, PixelWriter inPixelWriter, double inSideLength){
        Complex inCorner = new Complex(cornerReal, cornerImag);
        double unit = inSideLength / PIXEL_GRID_HEIGHT;
        Complex moveX = new Complex(unit, 0);
        Complex moveY = new Complex(0, unit);
        for (int x = 0; x < 600; x++){
            for(int y = 0; y < 600; y++){
                inCorner.subtract(moveY);
                inPixelWriter.setColor(x, y, pickColor(doIterations(inCorner)));
            }
            inCorner.setImag(cornerImag);
            inCorner.add(moveX);
        }
    }
}
