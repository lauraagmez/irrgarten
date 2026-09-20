
package irrgarten.UI;

import irrgarten.Directions;
import irrgarten.GameState;
import java.util.Scanner;


public class TextUI implements UI{
    
    private static Scanner in = new Scanner(System.in);
    
    private char readChar() {
        String s = in.nextLine();  
        return s.charAt(0);
    }
    
    @Override
    public Directions nextMove() {
        System.out.print("Where? ");
        
        Directions direction = Directions.DOWN;
        boolean gotInput = false;
        
        while (!gotInput) {
            char c = readChar();
            switch(c) {
                case 'w':
                    System.out.print(" UP\n");
                    direction = Directions.UP;
                    gotInput = true;
                    break;
                case 's':
                    System.out.print(" DOWN\n");
                    direction = Directions.DOWN;
                    gotInput = true;
                    break;
                case 'd':
                    System.out.print("RIGHT\n");
                    direction = Directions.RIGHT;
                    gotInput = true;
                    break;
                case 'a':
                    System.out.print(" LEFT\n");
                    direction = Directions.LEFT;
                    gotInput = true;    
                    break;
            }
        }    
        return direction;
    }
    @Override
    public void showGame(GameState gameState) {
        System.out.println("\n");

        // 1. Mostrar el laberinto
        System.out.println(gameState.getLabyrinth());
        
        //2. Mostrar jugadores y monstruos
        System.out.println("Jugadores: \n" + gameState.getPlayers());
        System.out.println("Monstruos: \n" + gameState.getMonsters());
        
        //Turno o ganador
        if (!gameState.isWinner()) {
            System.out.print("Current player: Player#" + gameState.getCurrentPlayer()+"\n");
            System.out.print("Log: \n" + gameState.getLog());
        }else{
            System.out.println("\nChampion Player #"+gameState.getCurrentPlayer()+ "!\n");

        }
    }
    
    
    
}
