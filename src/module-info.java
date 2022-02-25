module JavaFX_Chat {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
