module admeguimas.arvorebmais {
    requires javafx.controls;
    requires javafx.fxml;


    opens admeguimas.arvorebmais to javafx.fxml;
    exports admeguimas.arvorebmais;
}