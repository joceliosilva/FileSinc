/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.bean;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Jocelio
 */
public class FileMesage implements Serializable{
    private String cliente;
    private File file;

    public FileMesage(String cliente, File file) {
        this.cliente = cliente;
        this.file = file;
    }

    public FileMesage(String cliente) {
        this.cliente = cliente;
    }

    public FileMesage() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
}
