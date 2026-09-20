
package irrgarten.UI;

import irrgarten.Directions;
import irrgarten.GameState;

//Implementada por TestUI
public interface UI {
    public Directions nextMove();
    public void showGame(GameState gameState);
    
}
