package frd.utn.dds_tp3.entities;

import java.util.Date;

public class Movimiento {
    private Long id;
    private Date creado;
    private Date procesado;
    private int tipo;
    private int estado;
    private double importe;
    private int idCuenta;
    private String descripcion;

    public Movimiento(Long id, Date creado, Date procesado, int tipo, int estado, double importe, int idCuenta, String descripcion) {
        this.id = id;
        this.creado = creado;
        this.procesado = procesado;
        this.tipo = tipo;
        this.estado = estado;
        this.importe = importe;
        this.idCuenta = idCuenta;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

    public Date getProcesado() {
        return procesado;
    }

    public void setProcesado(Date procesado) {
        this.procesado = procesado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
