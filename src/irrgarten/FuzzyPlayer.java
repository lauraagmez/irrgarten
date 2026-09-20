
package irrgarten;
import java.util.ArrayList;

public class FuzzyPlayer extends Player{
    public FuzzyPlayer(Player other){
        super(other);
    }
    /**
     * Método que informa sobre la dirección en la que se va a mover el jugador.
     * Se elige de forma aleatoria entre la preferida(obtenida del método move 
     * de Player) y las posibles validMoves.
     * @param direction  Dirección en la que se quiere mover el fuzzyplayer
     * @param validMoves  Lista de movimientos válidos
     * @return  Devuelve la dirección en la que se moverá el jugador (si es válida)
     */
    @Override
    public Directions move(Directions direction, ArrayList<Directions> validMoves){
        Directions preference=super.move(direction, validMoves);
        return Dice.nextStep(preference, validMoves, this.getIntelligence());
    }
    
    @Override
    public float attack(){
        return sumWeapons() + Dice.intensity(getStrength()); 
    }
    @Override
    protected float defensiveEnergy(){ 
        return sumShields() + Dice.intensity(getIntelligence());
    }
    @Override
    public String toString(){
        String salida="(Fuzzy) ";
        salida+=super.toString();
        
        return salida;
    }
    
}
