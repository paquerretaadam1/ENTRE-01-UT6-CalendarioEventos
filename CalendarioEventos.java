import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario; 

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        if(calendario.containsKey(nuevo.getMes())){            
            calendario.put(nuevo.getMes(), insertarEnOrden(calendario.get(nuevo.getMes()), nuevo));
        }
        else{
            ArrayList<Evento> eventosDelMes = new ArrayList<>();
            eventosDelMes.add(nuevo);
            calendario.put(nuevo.getMes(), eventosDelMes);
        }

    }

    /**
     * 
     */
    private ArrayList<Evento> insertarEnOrden(ArrayList<Evento> eventosDelMes, Evento nuevo) {
        ArrayList<Evento> resul = new ArrayList<>();
        resul.addAll(eventosDelMes);
        int i = 0;
        while(i < eventosDelMes.size()){
            if(resul.get(i).antesDe(nuevo)){
                resul.add(i, nuevo);
                i = eventosDelMes.size();
            }
            else{
                i++;
            }
        }
        return resul;

    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Mes> claves = calendario.keySet();
        for(Mes clave: claves){
            sb.append(clave + "\n\n\n");
            ArrayList<Evento> eventos = calendario.get(clave);
            for(int i = 0; i < eventos.size(); i++){
                sb.append(eventos.get(i).toString() + "\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.containsKey(mes)){
            return calendario.get(mes).size();            
        }    
        return 0;    
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> resul = new TreeSet<>();
        
        // int i = 0;
        // while(i < calendario.size()){
        // if(){

        // }
        // }
        return resul;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        String nombre = "";
        int larga = 0;
        Set<Mes> claves = calendario.keySet();
        for(Mes clave: claves){
            ArrayList<Evento> eventos = calendario.get(clave);
            for(int i = 0; i < eventos.size(); i++){
                if(eventos.get(i).getDuracion() > larga){
                    nombre = eventos.get(i).getNombre();
                    larga = eventos.get(i).getDuracion();
                }
            }           
        }
        return nombre;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int totalBorrados = 0;
        for(int i = 0; i < meses.length; i++){
            if(calendario.containsKey(meses[i])){
                ArrayList<Evento> eventosMes = calendario.get(meses[i]);
                int aux = eventosMes.size();
                eventosMes.removeIf(aComprobar -> (dia == aComprobar.getDia()));
                totalBorrados += (aux - eventosMes.size());
                if(calendario.get(meses[i]).size() == 0){
                    calendario.remove(meses[i]);
                }
                else{
                    calendario.put(meses[i], eventosMes);                    
                }
            }
        }
        return totalBorrados;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
