package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import tn.esprit.entities.Order;
import tn.esprit.entities.Product;
import tn.esprit.entities.User;
import tn.esprit.services.OrderServiceImpl;
import tn.esprit.services.ProductServiceImpl;
import tn.esprit.services.UserServiceImpl;

// ⭐ AJOUTEZ CES IMPORTS LOG4J ⭐
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    // ⭐ AJOUTEZ CETTE LIGNE POUR LE LOGGER ⭐
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private ListView<Order> orderListView;

    private OrderServiceImpl orderService;
    private UserServiceImpl userService;
    private ProductServiceImpl productService;

    @FXML
    private void handleGoToProduct(ActionEvent event) {
        logger.info("🔄 Navigation vers la page Product demandée");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/product.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            logger.info("✅ Navigation vers Product réussie");
        } catch (IOException e) {
            logger.error("❌ Erreur lors de la navigation vers Product: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        logger.info("🔧 Initialisation du OrderController");
        try {
            orderService = new OrderServiceImpl();
            userService = new UserServiceImpl();
            productService = new ProductServiceImpl();

            ObservableList<User> users = FXCollections.observableArrayList(userService.getAll());
            userComboBox.setItems(users);

            ObservableList<Product> products = FXCollections.observableArrayList(productService.getAll());
            productListView.setItems(products);
            productListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            loadOrders();
            logger.info("✅ OrderController initialisé avec succès - {} utilisateurs, {} produits chargés",
                    users.size(), products.size());
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'initialisation du OrderController: {}", e.getMessage());
        }
    }

    private void loadOrders() {
        logger.debug("📦 Chargement des commandes");
        try {
            List<Order> orders = orderService.getAll();
            orderListView.setItems(FXCollections.observableArrayList(orders));
            logger.info("✅ {} commandes chargées avec succès", orders.size());
        } catch (Exception e) {
            logger.error("❌ Erreur lors du chargement des commandes: {}", e.getMessage());
        }
    }

    @FXML
    public void handleAddOrder() {
        logger.info("➕ Tentative d'ajout d'une nouvelle commande");

        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Product> selectedProducts = productListView.getSelectionModel().getSelectedItems();

        if (selectedUser == null || selectedProducts.isEmpty()) {
            logger.warn("⚠️ Données manquantes pour créer la commande - Utilisateur: {}, Produits sélectionnés: {}",
                    selectedUser, selectedProducts.size());
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un utilisateur et au moins un produit !");
            alert.showAndWait();
            return;
        }

        try {
            Order order = new Order(0, selectedUser, new ArrayList<>(selectedProducts));
            orderService.add(order);
            logger.info("✅ Commande créée avec succès - ID: {}, Utilisateur: {}, Produits: {}",
                    order.getIdOrder(), selectedUser.getUsername(), selectedProducts.size());

            loadOrders();
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la création de la commande: {}", e.getMessage());
        }
    }

    @FXML
    public void handleDeleteOrder() {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            logger.info("🗑️ Tentative de suppression de la commande ID: {}", selectedOrder.getIdOrder());
            try {
                orderService.delete(selectedOrder.getIdOrder());
                logger.info("✅ Commande ID: {} supprimée avec succès", selectedOrder.getIdOrder());
                loadOrders();
            } catch (Exception e) {
                logger.error("❌ Erreur lors de la suppression de la commande ID: {} - {}",
                        selectedOrder.getIdOrder(), e.getMessage());
            }
        } else {
            logger.warn("⚠️ Aucune commande sélectionnée pour la suppression");
        }
    }
}