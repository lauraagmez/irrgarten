
package irrgarten;
import java.util.ArrayList;

public class Game {
    private static final int MAX_ROUNDS=10;
    private int currentPlayerIndex;
    private String log; 
    
    //ATRIBUTOS DE REFERENCIA
    private ArrayList <Player> players;
    private ArrayList <Monster> monsters;
    private Player currentPlayer; 
    private Labyrinth labyrinth;
    
    //ATRIBUTOS PRIVADOS PARA EL LABERINTO DISEÑADO
    private static final int NUM_ROWS=5;
    private static final int NUM_COLS=5;
    private static final int NUM_MONSTERS=4;

    
    /**
     * Crea y prepara una nueva partida: inicializa la lista de jugadores,
     * determina quién comienza, crea el laberinto con una salida aleatoria
     * y configura los elementos del tablero (monstruos y bloques). Finalmente
     * distribuye a los jugadores por el laberinto.
     *
     * @param nplayers Número de jugadores participantes
     */
    public Game(int nplayers){
        //Player (char number, float intelligence, float strength)
        players=new ArrayList<>();
        for (int i=0; i<nplayers; i++){
            char number=(char)('0'+i);
            players.add(new Player(number, Dice.randomIntelligence(), Dice.randomStrength()));
        } 
        //Monster se añade en la configuracion
        monsters=new ArrayList<>();
        
        //Jugador con el turno
        currentPlayerIndex=Dice.whoStarts(nplayers);
        currentPlayer=players.get(currentPlayerIndex);
        
        //Labyrinth(int nRows, int nCols, int exitRow, int exitCol)
        int exitRow = Dice.randomPos(NUM_ROWS);
        int exitCol = Dice.randomPos(NUM_COLS);

        labyrinth = new Labyrinth(NUM_ROWS, NUM_COLS, exitRow, exitCol);
        log="NEW GAME \n\n";
        //En la clase Game se crea y configura el laberinto
        configureLabyrinth();
        labyrinth.spreadPlayers(players);
    }
    /**
     * Comprueba si la partida ha finalizado consultando el laberinto para
     * determinar si existe un ganador.
     *
     * @return true si la partida ha terminado con un ganador, false en caso contrario
     */
    public boolean finished(){
        return labyrinth.haveAWinner();
    }
    
    /**
     * Ejecuta las acciones correspondientes a un turno completo: determina la
     * dirección de movimiento válida, desplaza al jugador, resuelve combates
     * o intentos de resurrección y avanza al siguiente jugador si la partida
     * continúa.
     *
     * @param preferredDirection Dirección preferida por el jugador para intentar moverse
     * @return true si la partida ha finalizado tras este turno, false si continúa
     */
    public boolean nextStep(Directions preferredDirection){
        this.log="";
        if (!currentPlayer.dead()){
            Directions direction=actualDirection(preferredDirection);
            if (direction!=preferredDirection){
                logPlayerNoOrders();
            }
            Monster monster = labyrinth.putPlayer(direction, currentPlayer);
            if (monster==null){ //Si monster es null no hay combate
                logNoMonster();
            }else{
                GameCharacter winner = combat (monster);
                manageReward(winner);
            }
        }else{
            manageResurrection();
        }
        boolean endGame=finished();
        if (!endGame){
            nextPlayer();
        }
        
        return endGame;
    }
    /**
     * Construye un objeto {@code GameState} que representa el estado actual
     * de la partida (texto del laberinto, información de jugadores y
     * monstruos, índice del jugador actual, bandera de victoria y log).
     *
     * @return Instancia de {@code GameState} con los datos actuales del juego
     */
    public GameState getGameState(){

        String lab = this.labyrinth.toString();
        String play="";
        for (int i=0; i<this.players.size(); i++){
            play+=players.get(i).toString()+System.lineSeparator();
        }
        String mons="";
        for (int i=0; i<this.NUM_MONSTERS; i++){ //Añadimos solo los monstruos en el tablero
            mons+=monsters.get(i).toString()+System.lineSeparator();
        }

        //GameState(String labrynth, String players, String monsters, int currentPlayer, boolean winner, String log)
        return new GameState(lab, play, mons, this.currentPlayerIndex, finished(), this.log);
    }
    
    
    /**
     * Crea los monstruos necesarios y coloca en el laberinto los elementos
     * estáticos (bloques) y las posiciones iniciales de los enemigos.
     */
    private void configureLabyrinth(){

        //Añade monstruos
        //Monster (String name, float intelligence, float strength)
        for (int i=0; i<NUM_MONSTERS; i++){
            char letter=(char)('A'+i);
            String name= "Monster"+letter;
            monsters.add(new Monster(name, Dice.randomIntelligence(), Dice.randomStrength()));
        } 
        labyrinth.addMonster(1, 3, monsters.get(0));
        labyrinth.addMonster(3, 2, monsters.get(1));
        labyrinth.addMonster(0, 4, monsters.get(2));
        labyrinth.addMonster(4, 0, monsters.get(3));
        
        //Añade bloques de obstáculos
        labyrinth.addBlock(Orientation.HORIZONTAL, 2, 2, 3);
        labyrinth.addBlock(Orientation.VERTICAL, 3, 4, 2);
        
   
    }
    /**
     * Pasa el turno al siguiente jugador en el orden de la lista, actualizando
     * el índice y la referencia al jugador actual.
     */
    private void nextPlayer(){
        currentPlayerIndex=(currentPlayerIndex+1)% players.size();
        currentPlayer=players.get(currentPlayerIndex);
    }
    
    /**
     * Determina la dirección definitiva en la que intentará moverse el
     * jugador actual en función de su preferencia y las casillas válidas
     * disponibles desde su posición.
     *
     * @param preferredDirection Dirección preferida por el jugador
     * @return Dirección válida seleccionada para el movimiento
     */
    private Directions actualDirection(Directions preferredDirection){
        int currentRow = currentPlayer.getRow();
        int currentCol = currentPlayer.getCol();
        ArrayList <Directions> validMoves = labyrinth.validMoves(currentRow, currentCol);
        return currentPlayer.move(preferredDirection, validMoves);
    }
    
    /**
     * Resuelve un combate por turnos entre el jugador actual y el monstruo
     * pasado como parámetro. El enfrentamiento continúa hasta que uno
     * resulte derrotado o se alcance el número máximo de rondas permitidas.
     *
     * @param monster Monstruo contra el que combate el jugador
     * @return {@code GameCharacter} que indica el ganador (PLAYER o MONSTER)
     */
    private GameCharacter combat(Monster monster){
        int rounds=0; //Implica que no contamos la primera ronda, un jugador puede ganar con 0 rounds
        GameCharacter winner=GameCharacter.PLAYER;
        
        //Jugador ataca y Monstruo se defiende
        boolean lose = monster.defend(currentPlayer.attack());
        //loop: mientras no pierda y no se excedan las rondas maximas
        while (!lose && (rounds < MAX_ROUNDS)){
            rounds++;
            winner=GameCharacter.MONSTER;
            //Monstruo ataca y Jugador se defiende
            lose= this.currentPlayer.defend(monster.attack());
            //Si el jugador no pierde contraataca
            if (!lose){
                winner=GameCharacter.PLAYER;
                lose=monster.defend(currentPlayer.attack());
            }
        }
        //1.7.Mensaje: se llega a las maximas rondas
        logRounds(rounds, MAX_ROUNDS);
        
        return winner;
    }
    
    /**
     * Procesa las consecuencias de un combate: otorga recompensas al
     * jugador vencedor y registra en el log quién resultó ganador.
     *
     * @param winner Identificador del personaje ganador del combate
     */
    private void manageReward(GameCharacter winner){
        if (winner == GameCharacter.PLAYER){
            this.currentPlayer.receiveReward();
            logPlayerWon();
        }else{
            logMonsterWon();
        }
    }
    
    /**
     * Gestiona la posible resurrección del jugador actual mediante una tirada
     * de dados. En caso de éxito restaura al jugador y lo convierte en un
     * {@code FuzzyPlayer} dentro del laberinto; en caso de fallo anota el
     * turno como perdido.
     *
     * @see Labyrinth#transformToFuzzy
     */
    private void manageResurrection(){
        boolean resurrect=Dice.resurrectPlayer();
        if (resurrect){
            //Resucitamos al jugador
            this.currentPlayer.resurrect();
            this.logResurrected();
            //Se cambia en el array de player el actual por el nuevo FuzzyPlayer
            //creamos el nuevo a partir del actual
            FuzzyPlayer fuzzy = new FuzzyPlayer(this.currentPlayer);
            this.players.set(this.currentPlayerIndex, fuzzy);
            this.labyrinth.transformToFuzzy(fuzzy);
        }else{
            this.logPlayerSkipTurn();
        }
    }
    /**
     * Añade al log un mensaje indicando que el jugador actual ha ganado el
     * combate.
     */
    private void logPlayerWon(){
        this.log += "Player #"+this.currentPlayer.getNumber() + " won the combat!"+ System.lineSeparator();
    }
    /**
     * Añade al log un mensaje indicando que el monstruo ha resultado vencedor
     * en el combate frente al jugador actual.
     */
    private void logMonsterWon(){
        this.log += "The monster won the combat"+ System.lineSeparator();
    }
    /**
     * Registra en el log que el jugador ha sido resucitado y transformado en
     * un jugador difuso (fuzzy player).
     */
    private void logResurrected(){
        this.log += "Player #"+this.currentPlayer.getNumber() +" resurrected as a fuzzy player"+ System.lineSeparator(); 
    }
    /**
     * Anota en el log que el turno del jugador actual ha sido saltado por
     * encontrarse muerto y no haber podido resucitar.
     */
    private void logPlayerSkipTurn(){
        this.log += "Turn skipped! Player #"+ this.currentPlayer.getNumber() + " is dead"+ System.lineSeparator();
    }
    /**
     * Registra que el jugador no pudo cumplir la orden humana indicada y no
     * ejecutó la acción solicitada.
     */
    private void logPlayerNoOrders(){
        this.log += "Player #"+this.currentPlayer.getNumber() + " did not follow the human instructions (action failed)"+ System.lineSeparator();
    }
    /**
     * Añade al registro que el jugador se ha movido a una celda sin monstruo
     * o que no ha podido completar su movimiento.
     */
    private void logNoMonster(){
        this.log += "Player #"+this.currentPlayer.getNumber() + " has moved to an empty cell or could not move"+ System.lineSeparator();
    }
    /**
     * Registra en el log el número de rondas de combate disputadas frente al
     * máximo permitido.
     *
     * @param rounds Rondas disputadas
     * @param max Rondas máximas permitidas
     */
    private void logRounds(int rounds, int max){
        this.log += "There has been "+ rounds +" / "+max+" combat rounds "+ System.lineSeparator();
    }
}
