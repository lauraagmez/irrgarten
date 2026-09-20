
package irrgarten.main;

import irrgarten.controller.Controller;
import irrgarten.Game;
import irrgarten.UI.GraphicUI;

public class main {
    static public void main (String args[]){
        final int PLAYERS=2;
        Game game=new Game(PLAYERS);
        GraphicUI view=new GraphicUI();
        Controller controller= new Controller(game, view);
        
        controller.play();
    }
}
