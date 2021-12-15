/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.managing.system;

/**
 *
 * @author engmohammed
 */
public class BorrowModel {

    private int num;
    private int idM;
    private int idB;
    private String nameM;
    private String nameB;
    private String date;

    /*public BorrowModel(String nameM, String nameB, String date) {
        this.nameM = nameM;
        this.nameB = nameB;
        this.date = date;
    }*/

    public BorrowModel(int num , String nameM, String nameB, String date, int idM, int idB) {
        this.num = num;
        this.nameM = nameM;
        this.nameB = nameB;
        this.date = date;
        this.idM = idM;
        this.idB = idB;
    }

    public String getNameM() {
        return nameM;
    }

    public void setNameM(String nameM) {
        this.nameM = nameM;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public int getIdB() {
        return idB;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
