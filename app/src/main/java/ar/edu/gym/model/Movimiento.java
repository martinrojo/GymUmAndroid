package ar.edu.gym.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movimiento {

    @SerializedName("id")
    private Long id;

    @SerializedName("fechaEntrada")
    private Date fechaEntrada;

    @SerializedName("fechaSalida")
    private Date fechaSalida;

    @SerializedName("persona")
    private Persona persona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", persona=" + persona +
                '}';
    }

}
