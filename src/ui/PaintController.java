package ui;


import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
public class PaintController {
	
	@FXML
    private Canvas canvas;
    
    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private ChoiceBox<String> toolSelector;
    
    @FXML
    private Slider penWeight;
    
    
   
    
    private GraphicsContext ctx;

    
    private boolean pencil;
    private double currentX, currentY;
    private Color currentColor;
   
    

    
    
    @FXML
    private void initialize() {
    	ctx = canvas.getGraphicsContext2D();
    	pencil = false;
    	currentX = 0;
    	currentY = 0;
    	currentColor = Color.BLACK;
    	colorPicker.setValue(Color.BLACK);
    	
    	
    	
    	List<String> tools = new ArrayList<>();
    	tools.add("pencil");
    	tools.add("eraser");
    	
    	ObservableList<String> toolList = FXCollections.observableArrayList(tools);
    	toolSelector.setItems(toolList);
    	toolSelector.getSelectionModel().select(0);
    	ctx.setLineCap(StrokeLineCap.ROUND);
    	
    	
    	
    	
    	
    	
    }
    
    @FXML
    void changeColor(ActionEvent event) {
    	//currentColor = colorPicker.getTypeSelector();
    	currentColor = colorPicker.getValue();
    }
    
    @FXML
    void endPencil(MouseEvent event) {
    	pencil = false;
    	

    }

    @FXML
    void pencilDraw(MouseEvent event) {
    	
    	if (pencil) {
    		double x2 = event.getX();
    		double y2 = event.getY();
    		String tool = toolSelector.getSelectionModel().getSelectedItem();
    		switch (tool) {
    		case "pencil": 
    			drawLine(currentX, currentY, x2, y2); break;
    		case "eraser": 
    			erase(x2,y2); break;
    			default: break;
    		}
    		
    		
    		currentX = x2;
    		currentY = y2;
    		
    		
    	}
    }

    @FXML
    void startPencil(MouseEvent event) {
    	pencil = true;
    	currentX = event.getX();
    	currentY = event.getY();
    	
    	double x2 = event.getX();
		double y2 = event.getY();
		String tool = toolSelector.getSelectionModel().getSelectedItem();
		switch (tool) {
		case "pencil": 
			drawLine(currentX, currentY, x2, y2); break;
		case "eraser": 
			erase(x2,y2); break;
			default: break;
		}
    }
    
    //welcome to the jungle
    
    private void drawLine(double x1, double y1, double x2, double y2) {
    	ctx.beginPath();
    	ctx.moveTo(x1, y1);
    	
    	ctx.lineTo(x2, y2);
    	ctx.setLineWidth(penWeight.getValue());
    	
    	ctx.setStroke(Paint.valueOf(currentColor.toString()));
    	ctx.stroke();
    	ctx.closePath(); 
    
    	
    	
    }
    
    private void erase(double x, double y) {
    	ctx.moveTo(x, y);
    	ctx.clearRect(x,y, penWeight.getValue(),penWeight.getValue());
    }
    
    
    @FXML
    void shortcut(KeyEvent event) {
    	KeyCombination kc = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
    	
    	if (kc.match(event)) {
    		pasteImage();
    	}
    	
    	
    }
    
    private void pasteImage() {
    	Image img = Clipboard.getSystemClipboard().getImage();
    	
    	if (img != null) {
    		ctx.drawImage(img, 0, 0,canvas.getWidth(),canvas.getHeight());
    	}
    }
    

    @FXML
    void savePicture(ActionEvent event) throws IOException {
    	
    	FileChooser fc = new FileChooser();
    	
    	
    	ArrayList<String> extensions = new ArrayList<>();
    	extensions.add("*.png");
    	extensions.add("*.jpg");
    	ExtensionFilter ef1 = new ExtensionFilter("Image", extensions.get(0));
    	ExtensionFilter ef2 = new ExtensionFilter("Image", extensions.get(1));
    	fc.getExtensionFilters().add(ef1);
    	fc.getExtensionFilters().add(ef2);
    	
    	fc.setInitialDirectory(fc.getInitialDirectory());
    	
    	File file = fc.showSaveDialog(null);
    	
    	
    	
    	try {
    		int w = (int)Math.round(canvas.getWidth());
    		int h = (int)Math.round(canvas.getHeight());
    		WritableImage img = new WritableImage(w,h);
    		canvas.snapshot(null, img);
    		RenderedImage imgR = SwingFXUtils.fromFXImage(img, null);
    		String ext = (file.getName().contains(".png")? "png" : "jpg");
    		ImageIO.write(imgR, ext, file);
    	}catch(Error e) {
    		
    		
    }
}

}
