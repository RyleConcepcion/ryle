package Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import Model.Categories;
import Model.Datasource;
import Model.Product;

public class ViewProductController extends ProductsController {

    @FXML
    public TextField fieldViewProductName;
    public TextField fieldViewProductPrice;
    public TextField fieldViewProductQuantity;
    public ComboBox<Categories> fieldViewProductCategoryId;
    public TextArea fieldViewProductDescription;
    public Text viewProductName;

    @FXML
    private void initialize() {
        fieldViewProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));
    }

    public void fillViewingProductFields(int product_id) {
        Task<ObservableList<Product>> fillProductTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getOneProduct(product_id));
            }
        };

        fillProductTask.setOnSucceeded(e -> {
            viewProductName.setText("Viewing: " + fillProductTask.valueProperty().getValue().get(0).getName());
            fieldViewProductName.setText(fillProductTask.valueProperty().getValue().get(0).getName());
            fieldViewProductPrice.setText(String.valueOf(fillProductTask.valueProperty().getValue().get(0).getPrice()));
            fieldViewProductQuantity.setText(String.valueOf(fillProductTask.valueProperty().getValue().get(0).getQuantity()));
            fieldViewProductDescription.setText(fillProductTask.valueProperty().getValue().get(0).getDescription());

            Categories category = new Categories();
            category.setId(fillProductTask.valueProperty().getValue().get(0).getCategory_id());
            category.setName(fillProductTask.valueProperty().getValue().get(0).getCategory_name());
            fieldViewProductCategoryId.getSelectionModel().select(category);
        });

        new Thread(fillProductTask).start();
    }
}
