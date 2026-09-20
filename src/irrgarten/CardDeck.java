
package irrgarten;
import java.util.ArrayList;
import java.util.Collections;
//El tipo suministrado tiene que ser un Weapon o Shield
abstract class CardDeck <T extends CombatElement>{
    private ArrayList <T> cardDeck;
    protected static final int TAMANIO_MAX=50;
    
    public CardDeck(){
        cardDeck = new ArrayList();
    }
     /**
     * Método que añade todas las cartas del tipo corresopndiente a la baraja
     * delegando parate de su funcionalidad en el método addCards()
     */
    protected abstract void addCards();
     /**
     * Método que añade una carta del tipo correspondiente al contenedor
     * @param card Carta a añadir
     */
    protected void addCard( T card){
        this.cardDeck.add(card);
    }
     /**
     * Método que devuelve la primera carta del tipo correspondiente de la 
     * baraja de cartas. Si la baraja está vacía se llama a addCards y se baraja.
     * @return Primera carta de la baraja.
     */
    public T nextCard(){
        if (this.cardDeck.isEmpty()){
            //Rellenamos con las cartas correspondientes
            this.addCards();
            //Mezclamos
            Collections.shuffle(this.cardDeck);
        }
        //utilizamos la primera carta(devolvemos y eliminamos)
        T primera = this.cardDeck.get(0);
        this.cardDeck.remove(0);
        return primera;
        
    }
    
}
