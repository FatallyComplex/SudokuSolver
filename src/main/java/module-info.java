module com.example.sudokusolver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jetbrains.annotations;
    requires javatuples;
    requires javafx.media;

    opens com.example.sudokusolver to javafx.fxml;
    exports com.example.sudokusolver;
    exports com.example.sudokusolver.Controller;
    opens com.example.sudokusolver.Controller to javafx.fxml;
}