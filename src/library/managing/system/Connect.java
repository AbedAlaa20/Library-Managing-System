package library.managing.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

public class Connect {

    public static Connection connect() {

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://Library.accdb");
            return con;

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Does't Connect");
            return null;
        }
    }

    public static int loginMemeber(String username, String password) {

        Connection con = connect();

        try {

            PreparedStatement ps = con.prepareStatement("select idM from member where membername = '" + username + "' and pass = '" + password + "' ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idM");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        return -1;
    }

    public static int count(String col, String table) {

        Connection con = connect();

        try {

            PreparedStatement ps = con.prepareStatement("select count(" + col + ") from " + table);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Integer.parseInt(rs.getString(1));
            }

        } catch (Exception e) {
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static ObservableList<BookModel> getBooks() {
        Connection con = connect();
        ObservableList<BookModel> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = con.prepareStatement("select * from book");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BookModel(rs.getInt("idB"), rs.getString("nameB"), rs.getString("author"), rs.getInt("copies")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return list;
    }

    public static ObservableList<MemberModel> getMembers() {
        Connection con = connect();
        ObservableList<MemberModel> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = con.prepareStatement("select * from member");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MemberModel(rs.getInt("idM"), rs.getString("membername"), rs.getString("pass"), rs.getInt("age")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return list;
    }

    public static ObservableList<BorrowModel> getBorrows(int idM) {
        Connection con = connect();
        ObservableList<BorrowModel> list = FXCollections.observableArrayList();

        try {

            String query = "select member.idM, member.membername , book.idB , book.nameB  ,  borrow.num ,  borrow.idM , borrow.idB , borrow.dateBo "
                    + "from member, book , borrow  "
                    + "where member.idM = borrow.idM and  book.idB = borrow.idB ";

            if (idM != -1) {

                query = "select member.idM , member.membername ,  book.idB , book.nameB  ,  borrow.num ,  borrow.idM , borrow.idB , borrow.dateBo "
                        + "from member, book , borrow "
                        + "where  borrow.idM = member.idM  and  book.idB = borrow.idB and borrow.idM = " + idM;
            }

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BorrowModel(rs.getInt("num"), rs.getString("membername"), rs.getString("nameB"), rs.getString("dateBo"), rs.getInt("idM"), rs.getInt("idB")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return list;
    }

    public static int getLastId(String table) {

        Connection con = connect();

        try {

            String filed = "idB";

            if (table.equals("member")) {
                filed = "idM";
            }

            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("select " + filed + " from " + table);
            resultSet.afterLast();
            resultSet.previous();
            return resultSet.getInt(filed);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    public static boolean Insert(String table, String a, String b, String c) {

        Connection con = connect();

        try {

            String sql = "";
            if (table.equals("book")) {
                sql = "insert into book (nameB, author , copies ) values ( '" + a + "' , '" + b + "' , " + Integer.parseInt(c) + " )";
            } else if (table.equals("borrow")) {
                sql = "insert into borrow (idM, idB , dateBo ) values ( " + Integer.parseInt(a) + " , " + Integer.parseInt(b) + " , '" + c + "' )";
            } else if (table.equals("member")) {
                sql = "insert into member (membername, pass , age ) values ( '" + a + "' , '" + b + "' , " + Integer.parseInt(c) + " )";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            return !ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
            if (table.equals("member")) {
                JOptionPane.showMessageDialog(null, "Name Exists");
            }

        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static boolean delete(String table, String where) {
        Connection con = connect();

        try {
            PreparedStatement ps = con.prepareStatement("delete from " + table + " where " + where);
            return !ps.execute();

        } catch (Exception e) {
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return false;
    }

    public static boolean Update(String table, String where, String a, String b, int c) {
        Connection con = connect();

        String sql = "update " + table + " set nameB = '" + a + "' , author = '" + b + "' , copies = '" + c + "' where " + where;

        if (!a.equals("") && !b.equals("")) {

            if (table.equals("member")) {
                sql = "update " + table + " set membername = '" + a + "' , pass = '" + b + "' , age = '" + c + "' where " + where;
            }

        } else {
            sql = "update " + table + " set copies = '" + c + "' where " + where;
        }

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            return !ps.execute();

        } catch (Exception e) {
            if (table.equals("member")) {
                JOptionPane.showMessageDialog(null, "The name Member Exists");
            }
        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return false;
    }

}
