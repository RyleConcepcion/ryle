package Controllers.Admin;

import Utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import Model.Datasource;
import Model.Product;

import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ProductsController {

    @FXML
    public TextField fieldProductsSearch;
    @FXML
    public Text viewProductResponse;
    @FXML
    public GridPane formEditProductView;
    @FXML
    private StackPane productsContent;
    @FXML
    private TableView<Product> tableProductsPage;

    @FXML
    public void listProducts() {

        Task<ObservableList<Product>> getAllProductsTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllProducts(Datasource.ORDER_BY_NONE));
            }
        };

        tableProductsPage.itemsProperty().bind(getAllProductsTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllProductsTask).start();

    }

    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {

                    private final Button viewButton = new Button("View");

                    {
                        viewButton.getStyleClass().add("button");
                        viewButton.getStyleClass().add("xs");
                        viewButton.getStyleClass().add("info");
                        viewButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            btnViewProduct(productData.getId());
                        });
                    }

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            btnEditProduct(productData.getId());
                        });
                    }

                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + productData.getName() + " ?");
                            alert.setTitle("Delete " + productData.getName() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.get() == ButtonType.OK) {
                                System.out.println("Delete Product");
                                System.out.println("product id: " + productData.getId());
                                System.out.println("product name: " + productData.getName());
                                if (Datasource.getInstance().deleteSingleProduct(productData.getId())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(viewButton);
                        buttonsPane.getChildren().add(editButton);
                        buttonsPane.getChildren().add(deleteButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };
            }
        };

        colBtnEdit.setCellFactory(cellFactory);

        tableProductsPage.getColumns().add(colBtnEdit);

    }

    @FXML
    private void btnProductsSearchOnAction() {
        Task<ObservableList<Product>> searchProductsTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().searchProducts(fieldProductsSearch.getText().toLowerCase(), Datasource.ORDER_BY_NONE));
            }
        };
        tableProductsPage.itemsProperty().bind(searchProductsTask.valueProperty());

        new Thread(searchProductsTask).start();
    }

    @FXML
    private void btnAddProductOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/Fxml/Admin/addProduct.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);

    }

    @FXML
    private void btnEditProduct(int product_id) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/Fxml/Admin/editProduct.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);

        EditProductController controller = fxmlLoader.getController();
        controller.fillEditingProductFields(product_id);

    }

    @FXML
    private void btnViewProduct(int product_id) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/Fxml/Admin/viewProduct.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);

        ViewProductController controller = fxmlLoader.getController();
        controller.fillViewingProductFields(product_id);
    }

    @FXML
    boolean areProductInputsValid(String fieldAddProductName, String fieldAddProductDescription, String fieldAddProductPrice, String fieldAddProductQuantity, int productCategoryId) {

        System.out.println("TODO: Better validate inputs.");
        String errorMessage = "";

        if (productCategoryId == 0) {
            errorMessage += "Not valid category id!\n";
        }
        if (fieldAddProductName == null || fieldAddProductName.length() < 3) {
            errorMessage += "please enter a valid name!\n";
        }
        if (fieldAddProductDescription == null || fieldAddProductDescription.length() < 5) {
            errorMessage += "Description is not valid!\n";
        }
        if (!HelperMethods.validateProductPrice(fieldAddProductPrice)) {
            errorMessage += "Price is not valid!\n";
        }

        if (!HelperMethods.validateProductQuantity(fieldAddProductQuantity)) {
            errorMessage += "Not valid quantity!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }

    }

    public static TextFormatter<Double> formatDoubleField() {
//        Pattern validEditingState = Pattern.compile("^[0-9]+(|\\.)[0-9]+$");
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }
            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        return new TextFormatter<>(converter, 0.0, filter);
    }

    public static TextFormatter<Integer> formatIntField() {
//        Pattern validEditingState = Pattern.compile("-?(0|[1-9]\\d*)");
        Pattern validEditingState = Pattern.compile("^[0-9]+$");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        StringConverter<Integer> converter = new StringConverter<Integer>() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0 ;
                } else {
                    return Integer.valueOf(s);
                }
            }
            @Override
            public String toString(Integer d) {
                return d.toString();
            }
        };

        return new TextFormatter<>(converter, 0, filter);
    }
}