
package irrgarten;

public class WeaponCardDeck extends CardDeck<Weapon>{
    /**
     * Método que añade todas las cartas de tipo Weapon a la baraja
     * con un tamaño máximo de TAMANIO_MAX cartas
     */
    @Override 
    protected void addCards(){
        for(int i=0; i<WeaponCardDeck.TAMANIO_MAX; i++){
            this.addCard(new Weapon(Dice.weaponPower(), Dice.usesLeft()));
        }
    }
}
