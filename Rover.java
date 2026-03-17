package marsrovers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Rover{
    private String nombre;
    private double contadorFugasDetectadas;
    private int posicionX;
    private int posicionY;
    private int posicionInicialX;
    private int posicionInicialY;
    private double potenciaActual;
    private double potenciaInicial;
    private String codigo;
    private int maximoRecargas;
    private int recargasRealizadas;
    private List<List<String>> mandatosExitosos;
    private List<List<String>> mandatosNoExitosos;
    private double costoMovimiento;
    private double costoDeteccion;
    
    public Rover(String nombre){
        this(nombre, 25.0);
    }
    
    public Rover(String nombreP, double potencia){
        this.nombre = nombreP;
        potenciaInicial = potencia;
        potenciaActual = potencia;
        posicionInicialX = 0;
        posicionInicialY = 0;
        posicionX = posicionInicialX;
        posicionY = posicionInicialY;
        recargasRealizadas = 0;
        contadorFugasDetectadas = 0;
        mandatosExitosos = new ArrayList<>();
        mandatosNoExitosos = new ArrayList<>();
        costoMovimiento= 0.25;
        maximoRecargas = 5;
        codigo = "RVR-" + System.currentTimeMillis() % 100000;
    }
    
    public void moverArriba(){
        if (tienePotenciaSuficiente()){
            if(!deteccionFuga()){
                posicionY += 1;
                potenciaActual -= costoMovimiento;
                registrarMandato("Desplazamiento Arriba", "Posible");
            } else {
                registrarMandato("Desplazamiento Arriba", "No posible: fuga detectada");
            }
        }else{
            registrarMandato("Desplazamiento Arriba", "No posible: potencia insuficiente");
        }
    }
    
    public void moverAbajo(){
        if (tienePotenciaSuficiente()){
            if(!deteccionFuga()){
                posicionY -= 1;
                potenciaActual -= costoMovimiento;
                registrarMandato("Desplazamiento Abajo", "Posible");
            } else {
                registrarMandato("Desplazamiento Abajo", "No posible: fuga detectada");
            }
        }else{
            registrarMandato("Desplazamiento Abajo", "No posible: potencia insuficiente");
        }
    }
    
    public void moverDerecha(){
        if (tienePotenciaSuficiente()){
            if(!deteccionFuga()){
                posicionX += 1;
                potenciaActual -= costoMovimiento;
                registrarMandato("Desplazamiento Derecha", "Posible");
            } else {
                registrarMandato("Desplazamiento Derecha", "No posible: fuga detectada");
            }
        }else{
            registrarMandato("Desplazamiento Derecha", "No posible: potencia insuficiente");
        }
    }
    
    public void moverIzquierda(){
        if (tienePotenciaSuficiente()){
            if(!deteccionFuga()){
                posicionX -= 1;
                potenciaActual -= costoMovimiento;
                registrarMandato("Desplazamiento Izquierda", "Posible");
            } else {
                registrarMandato("Desplazamiento Izquierda", "No posible: fuga detectada");
            }
        }else{
            registrarMandato("Desplazamiento Izquierda", "No posible: potencia insuficiente");
        }
    }
    
    private boolean deteccionFuga(){
        contadorFugasDetectadas++;
        potenciaActual -= costoDeteccion;
        Random random = new Random();
        return random. nextDouble() >= 0.5;
    }
    
    public String conocerPosicionActual(){
        return "Posición actual(x,y):" + posicionX + "," + posicionY;
    }
    
    public double getPotenciaActual(){
        return potenciaActual;
    }
    
    public void recargarPotencia(double potencia) {
        if (tienePotenciaSuficiente()){
            potenciaActual += potencia;
            recargasRealizadas++;
            registrarMandato ("Recarga ("+potencia+")", "Posible");
        } else{
            registrarMandato ("Recarga ("+potencia+")", "No Posible: recargas agotadas");
        }
    }
    
    private boolean puedeRecargar(){
        return (recargasRealizadas < maximoRecargas) ? true : false;
    }
    
    private boolean tienePotenciaSuficiente() {
        double costoMinimo = costoMovimiento + costoDeteccion;
        return potenciaActual >= costoMinimo;
    }
    
    private String obtenerFechaHoraActual(){
       Date fecha = new Date(System.currentTimeMillis());
       DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
       return formatoFecha.format(fecha);
    }
    
    private void registrarMandato(String tipoMandato, String estadoMandato) {
        ArrayList<String>mandato = new ArrayList<>();
        mandato.add(tipoMandato);
        mandato.add(estadoMandato);
        mandato.add(obtenerFechaHoraActual());
        
        if("Posible".compareTo(estadoMandato)==0){
            mandatosExitosos.add(mandato);
        } else{
            mandatosNoExitosos.add(mandato);
        }
    }
    
    public String informacionMarsRover(){
        String msg = "";
        
        msg += "----INFORMACION DEL ROVER----\n";
        msg += "Código: " +codigo + "\n";
        msg += "Nombre: " + nombre + "\n";
        msg += "Potencia (inicial - disponible): " + String.format("%.2f / %.2f", potenciaInicial, potenciaActual) + "\n";
        msg += "Posición (inicial -> actual): (" + posicionInicialX + "," + posicionInicialY + ") → (" + posicionX + "," + posicionY + ")\n";        
        msg += "Costos (movimiento/detección): " + String.format("%.2f / %.2f", costoMovimiento, costoDeteccion) + "\n";        
        msg += "Recargas (realizadas/máximas): " + recargasRealizadas + "/" + maximoRecargas + "\n";        
        msg += "Detecciones de fuga realizadas: " + contadorFugasDetectadas + "\n";
        
        msg += "----------------------------\n\n";
        
        msg += "---- Registro de Mandatos EXITOSOS ----\n";
        
        msg += String.format(" %-4s %-17s %-30s %-20s%n", "N°", "Fecha", "Mandato", "Estado");
        
        for (int i = 0; i < mandatosExitosos.size(); i++) {
            List<String> m = mandatosExitosos.get(i);
        
            String tipo = (m.size() > 0) ? m.get(0) : "";
            String estado = (m.size() > 1) ? m.get(1) : "";
            String fecha = (m.size() > 2) ? m.get(2) : "";
        
            msg += String.format(" %-4d %-17s %-30s %-20s%n", (i + 1), fecha, tipo, estado);
        }
        
        if (mandatosExitosos.isEmpty()) {
            msg += "(sin registros)\n";
        }
        
        msg += "\n";
        
        msg += "---- Registro de Mandatos FALLIDOS ----\n";
        
        msg += String.format(" %-4s %-17s %-30s %-20s%n", "N°", "Fecha", "Mandato", "Estado");
        
        for (int i = 0; i < mandatosNoExitosos.size(); i++) {
            List<String> m = mandatosNoExitosos.get(i);
        
            String tipo = (m.size() > 0) ? m.get(0) : "";
            String estado = (m.size() > 1) ? m.get(1) : "";
            String fecha = (m.size() > 2) ? m.get(2) : "";
        
            msg += String.format(" %-4d %-17s %-30s %-20s%n", (i + 1), fecha, tipo, estado);
        }
        
        if (mandatosNoExitosos.isEmpty()) {
            msg += "(sin registros)\n";
        }
        
        return msg;
        
    }
}

