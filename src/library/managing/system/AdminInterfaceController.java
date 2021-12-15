/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.managing.system;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class AdminInterfaceController implements Initializable {

    /*                  Book                */
    @FXML
    private Pane booksPane;

    @FXML
    private TextField bookName;

    @FXML
    private TextField bookCopies;

    @FXML
    private TextField bookAuthor;

    @FXML
    TableView<BookModel> bookTable;
    @FXML
    TableColumn<BookModel, Integer> numB;
    @FXML
    TableColumn<BookModel, String> nameB;
    @FXML
    TableColumn<BookModel, Integer> copiesB;
    @FXML
    TableColumn<BookModel, String> authorB;

    ObservableList<BookModel> books;

    int indexB = -1;

    /*                  Member                */
    @FXML
    private Pane memberPane;

    @FXML
    private TextField memberName;

    @FXML
    private TextField memberPass;

    @FXML
    private TextField memberAge;

    @FXML
    TableView<MemberModel> memberTable;
    @FXML
    TableColumn<MemberModel, Integer> numM;
    @FXML
    TableColumn<MemberModel, String> nameM;
    @FXML
    TableColumn<MemberModel, Integer> ageM;
    @FXML
    TableColumn<MemberModel, String> passM;

    ObservableList<MemberModel> members;

    int indexM = -1;

    /*                  Borrow                */
    @FXML
    private Pane borrowPane;

    @FXML
    TableView<BorrowModel> borrowTable;
    @FXML
    TableColumn<BorrowModel, String> nameMB;
    @FXML
    TableColumn<BorrowModel, String> nameBB;
    @FXML
    TableColumn<BorrowModel, String> dateBorrow;

    ObservableList<BorrowModel> borrows;

    public void showBook() {
        booksPane.setVisible(true);
        memberPane.setVisible(false);
        borrowPane.setVisible(false);

        bookTable.setItems(books);
    }

    public void showMember() {
        booksPane.setVisible(false);
        memberPane.setVisible(true);
        borrowPane.setVisible(false);

        memberTable.setItems(members);
    }

    public void showBorrow() {
        booksPane.setVisible(false);
        memberPane.setVisible(false);
        borrowPane.setVisible(true);

        borrows = Connect.getBorrows(-1);
        borrowTable.setItems(borrows);
    }

    public void addBook() {

        String name = bookName.getText().trim();
        String author = bookAuthor.getText().trim();

        if (!name.equals("") && !author.equals("") && !bookCopies.getText().trim().equals("")) {

            int copies = Integer.parseInt(bookCopies.getText());

            if (Connect.Insert("book", name, author, copies + "")) {

                int id = Connect.getLastId("book");
                books.add(new BookModel(id, name, author, copies));
                JOptionPane.showMessageDialog(null, "Done Add");
                clearText(bookName, bookAuthor, bookCopies);
            }

        } else {
            JOptionPane.showMessageDialog(null, "The Filed Empty");
        }
    }

    public void updateBook() {

        try {
            if (Connect.Update("book", "idB = " + numB.getCellData(indexB), bookName.getText(), bookAuthor.getText(), Integer.parseInt(bookCopies.getText()))) {
                books.set(indexB, new BookModel(numB.getCellData(indexB), bookName.getText(), bookAuthor.getText(), Integer.parseInt(bookCopies.getText())));
                clearText(bookName, bookAuthor, bookCopies);
                indexM = -1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select Book from table");
        }
    }

    public void deleteBook() {
        if (indexB <= -1) {
            return;
        }

        if (Connect.delete("book", "idB = " + numB.getCellData(indexB))) {
            Connect.delete("borrow", "idB = " + numB.getCellData(indexB));
            books.remove(indexB);
            indexB = -1;
            clearText(bookName, bookAuthor, bookCopies);
        }
    }

    public void getSelectBooks() {

        indexB = bookTable.getSelectionModel().getSelectedIndex();

        if (indexB < 0) {
            return;
        }

        bookName.setText(nameB.getCellData(indexB).toString());
        bookAuthor.setText(authorB.getCellData(indexB).toString());
        bookCopies.setText(copiesB.getCellData(indexB).toString());

    }

    public void addMember() {
        String name = memberName.getText().trim();
        String pass = memberPass.getText().trim();

        if (!name.equals("") && !pass.equals("") && !memberAge.getText().trim().equals("")) {

            int age = Integer.parseInt(memberAge.getText());

            if (Connect.Insert("member", name, pass, age + "")) {

                int id = Connect.getLastId("member");
                members.add(new MemberModel(id, name, pass, age));
                JOptionPane.showMessageDialog(null, "Done Add");
                clearText(memberName, memberPass, memberAge);
            }

        } else {
            JOptionPane.showMessageDialog(null, "The Filed Empty");
        }
    }

    public void updateMember() {
        try {
            if (Connect.Update("member", "idM = " + numM.getCellData(indexM), memberName.getText(), memberPass.getText(), Integer.parseInt(memberAge.getText()))) {
                members.set(indexM, new MemberModel(numM.getCellData(indexM), memberName.getText(), memberPass.getText(), Integer.parseInt(memberAge.getText())));
                clearText(memberName, memberPass, memberAge);
                indexM = -1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please select member from table");
        }
    }

    public void deleteMember() {
        if (indexM <= -1) {
            return;
        }

        if (Connect.delete("member", "idM = " + numM.getCellData(indexM))) {
            updateCopiesBook(numM.getCellData(indexM));
            Connect.delete("borrow", "idM = " + numM.getCellData(indexM));
            members.remove(indexM);
            indexM = -1;
            clearText(memberName, memberPass, memberAge);
        }
    }

    public void getSelectMembers() {

        indexM = memberTable.getSelectionModel().getSelectedIndex();

        if (indexM < 0) {
            return;
        }

        memberName.setText(nameM.getCellData(indexM).toString());
        memberPass.setText(passM.getCellData(indexM).toString());
        memberAge.setText(ageM.getCellData(indexM).toString());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        numB.setCellValueFactory(new PropertyValueFactory<>("idB"));
        nameB.setCellValueFactory(new PropertyValueFactory<>("name"));
        copiesB.setCellValueFactory(new PropertyValueFactory<>("copies"));
        authorB.setCellValueFactory(new PropertyValueFactory<>("author"));

        numM.setCellValueFactory(new PropertyValueFactory<>("idM"));
        nameM.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageM.setCellValueFactory(new PropertyValueFactory<>("age"));
        passM.setCellValueFactory(new PropertyValueFactory<>("pass"));

        nameMB.setCellValueFactory(new PropertyValueFactory<>("nameM"));
        nameBB.setCellValueFactory(new PropertyValueFactory<>("nameB"));
        dateBorrow.setCellValueFactory(new PropertyValueFactory<>("date"));

        books = Connect.getBooks();
        members = Connect.getMembers();
        borrows = Connect.getBorrows(-1);

    }

    public void logout(ActionEvent e) {
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
            ee.printStackTrace();
        }
    }

    public void clearText(TextField t1, TextField t2, TextField t3) {
        t1.clear();
        t2.clear();
        t3.clear();
    }

    public void updateCopiesBook(int idM) {

        ObservableList<BorrowModel> borrow = FXCollections.observableArrayList();

        for (int i = 0; i < borrows.size(); i++) {

            if (borrows.get(i).getIdM() == idM) {

                searchBookAndIncrease(borrows.get(i).getIdB());
                borrow.add(borrows.get(i));

            }

        }

        for (int j = 0; j < borrow.size(); j++) {
            borrows.remove(borrow.get(j));
        }

    }

    public void searchBookAndIncrease(int idB) {

        for (int i = 0; i < books.size(); i++) {

            if (books.get(i).getIdB() == idB) {

                BookModel bm = books.get(i);
                bm.setCopies(books.get(i).getCopies() + 1);

                books.set(i, new BookModel(bm.getIdB(), bm.getName(), bm.getAuthor(), bm.getCopies()));

                Connect.Update("book", "idB = " + idB, "", "", books.get(i).getCopies());

            }

        }
    }

}
