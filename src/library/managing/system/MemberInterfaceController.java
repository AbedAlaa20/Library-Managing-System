/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.managing.system;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author engmohammed
 */
public class MemberInterfaceController implements Initializable {

    @FXML
    Pane borrowBook;

    @FXML
    Pane viewReturnBook;

    @FXML
    TextField searchBook;

    @FXML
    TableView<BookModel> borrowTable;
    @FXML
    TableColumn<BookModel, Integer> numB;
    @FXML
    TableColumn<BookModel, String> nameB;
    @FXML
    TableColumn<BookModel, Integer> copiesB;
    @FXML
    TableColumn<BookModel, String> authorB;

    ObservableList<BookModel> books;

    @FXML
    TableView<BorrowModel> returnTable;
    @FXML
    TableColumn<BorrowModel, Integer> numBo;
    @FXML
    TableColumn<BorrowModel, String> nameBB;
    @FXML
    TableColumn<BorrowModel, String> dateBorrow;

    ObservableList<BorrowModel> borrows;

    @FXML
    CheckBox author;

    int index;

    public void showBorrowBook() {
        borrowBook.setVisible(true);
        viewReturnBook.setVisible(false);
        index = -1;
        borrowTable.setItems(books);

    }

    public void showView_ReturnBook() {
        borrowBook.setVisible(false);
        viewReturnBook.setVisible(true);
        index = -1;

        borrows = Connect.getBorrows(LoginController.idM);
        returnTable.setItems(borrows);
    }

    public void selectRow() {

        if (borrowBook.isVisible()) {
            index = borrowTable.getSelectionModel().getSelectedIndex();
        } else {
            index = returnTable.getSelectionModel().getSelectedIndex();
        }

    }

    public void borrowBook() {

        if (index == -1) {
            return;
        }

        if (copiesB.getCellData(index) > 0) {

            if (!searchBorrowBook(numB.getCellData(index))) {

                BookModel bm = books.get(index);
                bm.setCopies(bm.getCopies() - 1);
                books.set(index, bm);

                Connect.Update("book", "idB = " + numB.getCellData(index), "", "", bm.getCopies());

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                Connect.Insert("borrow", LoginController.idM + "", numB.getCellData(index) + "", dateFormat.format(date));

                index = -1;
                borrowTable.getSelectionModel().clearSelection();
                borrows = Connect.getBorrows(LoginController.idM);

                JOptionPane.showMessageDialog(null, "Done Borrow");

            } else {
                JOptionPane.showMessageDialog(null, "The book has been borrowed before");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Not available, Number of copies is zero");
        }

    }

    public void returnBook() {

        if (index == -1) {
            return;
        }

        if (Connect.delete("borrow", "num = " + numBo.getCellData(index))) {

            int indexBook = searchBook(borrows.get(index).getIdB());

            BookModel bm = books.get(indexBook);
            bm.setCopies(bm.getCopies() + 1);
            books.set(indexBook, bm);

            Connect.Update("book", "idB = " + borrows.get(index).getIdB(), "", "", bm.getCopies());

            borrows.remove(index);

            JOptionPane.showMessageDialog(null, "Done Return Book");

        }

    }

    public int searchBook(int idB) {

        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getIdB() == idB) {

                return i;

            }

        }

        return 0;
    }

    public boolean searchBorrowBook(int idB) {

        for (int i = 0; i < borrows.size(); i++) {

            if (borrows.get(i).getIdB() == idB) {
                return true;
            }

        }

        return false;
    }

    public void clearSearch() {
        searchBook.setText("");
    }

    public void SearchBook() {

        searchBook.textProperty().addListener(new InvalidationListener() {

            @Override

            public void invalidated(Observable o) {

                if (searchBook.textProperty().get().isEmpty()) {

                    borrowTable.setItems(books);

                    return;

                }

                ObservableList<BookModel> tableItems = FXCollections.observableArrayList();

                TableColumn col = borrowTable.getColumns().get(1);

                if (author.isSelected()) {
                    col = borrowTable.getColumns().get(3);
                }

                for (int i = 0; i < books.size(); i++) {

                    for (int j = 0; j < borrowTable.getColumns().size(); j++) {

                        String cellValue = col.getCellData(books.get(i)).toString();

                        cellValue = cellValue.toLowerCase();

                        if (cellValue.contains(searchBook.getText().toLowerCase()) && cellValue.startsWith(searchBook.getText().toLowerCase())) {

                            tableItems.add(books.get(i));

                            break;

                        }
                    }
                }

                borrowTable.setItems(tableItems);

            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        numB.setCellValueFactory(new PropertyValueFactory<BookModel, Integer>("idB"));
        nameB.setCellValueFactory(new PropertyValueFactory<BookModel, String>("name"));
        copiesB.setCellValueFactory(new PropertyValueFactory<BookModel, Integer>("copies"));
        authorB.setCellValueFactory(new PropertyValueFactory<BookModel, String>("author"));

        numBo.setCellValueFactory(new PropertyValueFactory<BorrowModel, Integer>("num"));
        nameBB.setCellValueFactory(new PropertyValueFactory<BorrowModel, String>("nameB"));
        dateBorrow.setCellValueFactory(new PropertyValueFactory<BorrowModel, String>("date"));

        books = Connect.getBooks();
        borrows = Connect.getBorrows(LoginController.idM);

    }

    public void logout(Event e) {
        try {

            Parent blah = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(blah);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            appStage.setScene(scene);
            appStage.setTitle("Login");

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            appStage.setX((primScreenBounds.getWidth() - appStage.getWidth()) / 2);
            appStage.setY((primScreenBounds.getHeight() - appStage.getHeight()) / 2);

        } catch (Exception ee) {
        }
    }

}
