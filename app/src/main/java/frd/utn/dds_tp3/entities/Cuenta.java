package frd.utn.dds_tp3.entities;

import java.util.Date;

public class Cuenta {

    private Long id;
    private long numero;
    private Date apertura;
    private int idCliente;
    private double saldo;

    public Cuenta(Long id, long numero, Date apertura, int idCliente, double saldo) {
        this.id = id;
        this.numero = numero;
        this.apertura = apertura;
        this.idCliente = idCliente;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public Date getApertura() {
        return apertura;
    }

    public void setApertura(Date apertura) {
        this.apertura = apertura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String toString(){
        return ""+ this.numero;
    }
}
