package dulceamor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;

public class SistemaPedidosFX extends Application {

    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Producto> productos = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dulce Amor - Gestión de Pedidos");

        Button btnClientes = new Button("Registrar Cliente");
        Button btnProductos = new Button("Registrar Producto");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(btnClientes, btnProductos);
        layout.setStyle("-fx-padding: 20;");

        btnClientes.setOnAction(e -> mostrarFormularioCliente());
        btnProductos.setOnAction(e -> mostrarFormularioProducto());

        Scene scene = new Scene(layout, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarFormularioCliente() {
        Stage ventana = new Stage();
        ventana.setTitle("Registrar Cliente");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre completo");
        TextField telefono = new TextField();
        telefono.setPromptText("Teléfono");
        TextField direccion = new TextField();
        direccion.setPromptText("Dirección");
        TextField email = new TextField();
        email.setPromptText("Email");

        Button guardar = new Button("Guardar");
guardar.setOnAction(e -> {
    try (Connection conn = ConexionBD.conectar()) {
        String sql = "INSERT INTO clientes (nombre, telefono, direccion, email) VALUES (?, ?, ?, ?)";
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombre.getText());
        stmt.setString(2, telefono.getText());
        stmt.setString(3, direccion.getText());
        stmt.setString(4, email.getText());
        stmt.executeUpdate();

        nombre.clear(); telefono.clear(); direccion.clear(); email.clear();
    } catch (SQLException ex) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText("Error al guardar en la base de datos.");
        alerta.showAndWait();
    }
});


        Button verLista = new Button("Ver lista de clientes");
        verLista.setOnAction(e -> mostrarListaClientes());

        HBox botones = new HBox(10, guardar, verLista);

        VBox layout = new VBox(10, nombre, telefono, direccion, email, botones);
        layout.setStyle("-fx-padding: 20;");

        ventana.setScene(new Scene(layout));
        ventana.show();
    }

    private void mostrarListaClientes() {
        Stage ventana = new Stage();
        ventana.setTitle("Lista de Clientes");

        ListView<ClienteItem> lista = new ListView<>();

try (Connection conn = ConexionBD.conectar()) {
    String sql = "SELECT id_cliente, nombre, telefono, direccion, email FROM clientes";
    java.sql.Statement stmt = conn.createStatement();
    java.sql.ResultSet rs = stmt.executeQuery(sql);

while (rs.next()) {
    int id = rs.getInt("id_cliente");
    String linea = rs.getString("nombre") + " | " +
                   rs.getString("telefono") + " | " +
                   rs.getString("direccion") + " | " +
                   rs.getString("email");
    lista.getItems().add(new ClienteItem(id, linea));
}

} catch (Exception ex) {
    Alert alerta = new Alert(Alert.AlertType.ERROR);
    alerta.setContentText("Error al cargar clientes: " + ex.getMessage());
    alerta.showAndWait();
}


   Button eliminar = new Button("Eliminar seleccionado");
    eliminar.setOnAction(e -> {
    ClienteItem seleccionado = lista.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "DELETE FROM clientes WHERE id_cliente = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, seleccionado.getId());
            stmt.executeUpdate();
            lista.getItems().remove(seleccionado);
        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Error al eliminar cliente: " + ex.getMessage());
            alerta.showAndWait();
        }
    } else {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setContentText("Seleccioná un cliente para eliminar.");
        alerta.showAndWait();
    }
});

Button modificar = new Button("Modificar seleccionado");
modificar.setOnAction(e -> {
    ClienteItem seleccionado = lista.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        try (Connection conn = ConexionBD.conectar()) {
            String query = "SELECT * FROM clientes WHERE id_cliente = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, seleccionado.getId());
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreActual = rs.getString("nombre");
                String telefonoActual = rs.getString("telefono");
                String direccionActual = rs.getString("direccion");
                String emailActual = rs.getString("email");

 
                Stage editar = new Stage();
                editar.setTitle("Modificar Cliente");

                TextField nombre = new TextField(nombreActual);
                TextField telefono = new TextField(telefonoActual);
                TextField direccion = new TextField(direccionActual);
                TextField email = new TextField(emailActual);

                Button guardarCambios = new Button("Guardar cambios");
                guardarCambios.setOnAction(ev -> {
                    try (Connection conn2 = ConexionBD.conectar()) {
                        String update = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ?, email = ? WHERE id_cliente = ?";
                        java.sql.PreparedStatement stmt2 = conn2.prepareStatement(update);
                        stmt2.setString(1, nombre.getText());
                        stmt2.setString(2, telefono.getText());
                        stmt2.setString(3, direccion.getText());
                        stmt2.setString(4, email.getText());
                        stmt2.setInt(5, seleccionado.getId());
                        stmt2.executeUpdate();

                        editar.close();
                        mostrarListaClientes(); // recargamos la vista
                    } catch (Exception ex2) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setContentText("Error al actualizar cliente: " + ex2.getMessage());
                        alerta.showAndWait();
                    }
                });

                VBox layoutEditar = new VBox(10, nombre, telefono, direccion, email, guardarCambios);
                layoutEditar.setStyle("-fx-padding: 20;");
                editar.setScene(new Scene(layoutEditar));
                editar.show();
            }

        } catch (Exception ex) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Error al cargar cliente: " + ex.getMessage());
            alerta.showAndWait();
        }

    } else {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setContentText("Seleccioná un cliente para modificar.");
        alerta.showAndWait();
    }
});


        HBox botones = new HBox(10, eliminar, modificar);

        VBox layout = new VBox(10, lista, botones);
        layout.setStyle("-fx-padding: 20;");

        ventana.setScene(new Scene(layout, 500, 350));
        ventana.show();
    }

    private void mostrarFormularioProducto() {
        Stage ventana = new Stage();
        ventana.setTitle("Registrar Producto");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del producto");
        TextField precio = new TextField();
        precio.setPromptText("Precio");
        TextField stock = new TextField();
        stock.setPromptText("Stock disponible");

        Button guardar = new Button("Guardar");
        guardar.setOnAction(e -> {
            try {
                double p = Double.parseDouble(precio.getText());
                int s = Integer.parseInt(stock.getText());
                Producto prod = new Producto(nombre.getText(), p, s);
                productos.add(prod);
                ventana.close();
            } catch (NumberFormatException ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Precio y stock deben ser valores numéricos.");
                alerta.showAndWait();
            }
        });

        VBox layout = new VBox(10, nombre, precio, stock, guardar);
        layout.setStyle("-fx-padding: 20;");

        ventana.setScene(new Scene(layout));
        ventana.show();
    }
}

class Persona {
    protected String nombre;

    public Persona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

class Cliente extends Persona {
    private String telefono;
    private String direccion;
    private String email;

    public Cliente(String nombre, String telefono, String direccion, String email) {
        super(nombre);
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class Producto {
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }
}
