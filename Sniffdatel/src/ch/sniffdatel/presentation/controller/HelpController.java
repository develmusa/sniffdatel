package ch.sniffdatel.presentation.controller;

import java.net.MalformedURLException;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class HelpController {
 	@FXML
 	private WebView webView;
 	
 	@FXML
 	private void initialize() throws MalformedURLException{
 		webView.getEngine().load(HelpController.class.getResource("/help/help.html").toString());
 	}
 }
