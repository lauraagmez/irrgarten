
package irrgarten;

import java.util.ArrayList;


public class Labyrinth {
    private static final char BLOCK_CHAR='X';
    private static final char EMPTY_CHAR='-';
    private static final char MONSTER_CHAR='M';
    private static final char COMBAT_CHAR='C';
    private static final char EXIT_CHAR='E';
    private static final int ROW=0; //¿PARA QUE SIRVEEEEEEEEEE?
    private static final int COL=1;
    private int nRows;
    private int nCols;
    private int exitRow;
    private int exitCol;
    private char[][] labyrinth;
    private Player[][] players;
    private Monster[][] monsters;
    
    public Labyrinth(int nRows, int nCols, int exitRow, int exitCol){
        this.nRows=nRows;
        this.nCols=nCols;
        this.exitRow=exitRow;
        this.exitCol=exitCol;
        
        //Inicializacion de las matrices
        this.labyrinth=new char[nRows][nCols];
        this.players=new Player[nRows][nCols];
        this.monsters=new Monster[nRows][nCols];
        for (int i=0; i<nRows; ++i){
            for (int j=0; j<nCols; ++j){
                labyrinth[i][j]=EMPTY_CHAR;
                players[i][j]=null;
                monsters[i][j]=null;
            }
        }
        this.labyrinth[exitRow][exitCol]=EXIT_CHAR;
    }
    /**
     * Reparte un conjunto de jugadores en diferentes posiciones del laberinto.
     * @param players Lista de jugadores que serán ubicados en el laberinto
     */
    public void spreadPlayers(ArrayList <Player> players){
        int oldRow=-1;
        int oldCol=-1;
        
        for (Player p : players){
            int[]pos=randomEmptyPos();
            Monster m =putPlayer2D(oldRow, oldCol, pos[ROW], pos[COL], p);
        }
    }
    /**
     * Verifica si algún jugador ha alcanzado la salida y ha completado el juego exitosamente.
     * @return true si existe un ganador, false de lo contrario
     */
    public boolean haveAWinner(){
        //Si no está vacía tenemos un ganador
        //SI UN MONSTRUO PUEDE ESTAR EN LA EXIT ESTO NO FUNCIONARIA
        return this.players[this.exitRow][this.exitCol]!=null;
    }
    /**
     * Retorna una representación textual que refleja la configuración actual de cada casilla
     * del laberinto, incluyendo la presencia de monstruos, combates, etc.
     * @return Texto que describe el estado de todas las casillas del laberinto
     */
    @Override
    public String toString(){
        String laby="";
        final String CELL_FORMAT = "%-4s"; 
    
        // Indices del laberinto
        laby += String.format(CELL_FORMAT, " ");
        for (int j=0; j<this.nCols; j++){
            laby += String.format(CELL_FORMAT, String.valueOf(j));
        }
        laby += "\n";

        //Contenido del Laberinto
        for (int i=0; i<nRows; i++){
            laby += String.format(CELL_FORMAT, String.valueOf(i)); // Índice de Fila

            for (int j=0; j<nCols; j++){
                String cell; 
                // Celdas
                if (labyrinth[i][j] == BLOCK_CHAR) { 
                    cell = String.valueOf(BLOCK_CHAR); // "X"
                }
                else if (emptyPos(i, j)){
                    cell = String.valueOf(EMPTY_CHAR); // "-"
                }else if (monsterPos(i, j)){
                    cell = String.valueOf(MONSTER_CHAR); // "M"
                }else if (exitPos(i, j)){
                    cell = String.valueOf(EXIT_CHAR); // "E"
                }else if (combatPos (i, j)){
                    cell = String.valueOf(COMBAT_CHAR); // "C"
                }else{
                    // Si llegamos aquí, es un jugador
                    cell = "P" + labyrinth[i][j]; 
                }

                laby += String.format(CELL_FORMAT, cell);
            }
            laby += "\n";
        }
                
        return laby;
    }
    /**
     * Sitúa un monstruo en una casilla específica del laberinto, siempre que la ubicación
     * sea válida y no esté ocupada.
     * @param row Número de fila donde se colocará el monstruo
     * @param col Número de columna donde se colocará el monstruo
     * @param monster Monstruo a insertar
     */
    public void addMonster(int row, int col, Monster monster){
        if (posOK(row, col) && emptyPos(row, col)){
            //Indica al monstruo su posicion actual
            monster.setPos(row, col);
            //Anota en el laberinto la presentcia de un monstruo
            labyrinth[row][col]=MONSTER_CHAR;
            //Guarda la referencia en el contenedor atributo adecuado
            monsters[row][col]=monster;
        }
    }
    /**
     * Traslada un jugador en una dirección específica dentro del laberinto.
     * Retorna información sobre posibles encuentros con monstruos.
     * @param direction Dirección del movimiento (si es inválida, no ocurre el desplazamiento)
     * @param player Jugador a mover
     * @return El monstruo encontrado, o null si no hay encuentro
     */
    public Monster putPlayer(Directions direction, Player player){
        //obtiene la posicion actual
        int oldRow = player.getRow();
        int oldCol = player.getCol();
        //convierte la posicion en un cambio de coordenadas
        int [] newPos = dir2Pos(oldRow, oldCol, direction);
        //decide si el movimiento es valido actualiza las matrices internas y detecta si el jugador cae sobre un monstruo
        Monster monster = putPlayer2D (oldRow, oldCol, newPos[ROW], newPos[COL], player);
        return monster;
    }
    /**
     * Inserta una barrera en el laberinto con una orientación y dimensiones determinadas.
     * @param orientation Disposición del bloque (vertical o horizontal)
     * @param startRow Fila inicial
     * @param startCol Columna inicial
     * @param length Extensión del bloque
     */
    public void addBlock(Orientation orientation, int startRow, int startCol, int length){
        int incRow=0;
        int incCol=0;
        if (orientation == Orientation.VERTICAL){
            incRow=1;
        }else{
            incCol=1;
        }
        int row=startRow;
        int col=startCol;
        while ((posOK(row, col) && emptyPos(row, col))&&length>0){
            labyrinth[row][col]= BLOCK_CHAR;
            length-=1;
            row+=incRow;
            col+=incCol;
        }
        
    }
    /**
     * Determina qué movimientos son factibles desde una posición particular del laberinto.
     * @param row Fila de referencia
     * @param col Columna de referencia
     * @return Lista de direcciones viables desde esa posición
     */
    public ArrayList <Directions> validMoves(int row, int col){
        ArrayList <Directions> output =new ArrayList<>();
        if(canStepOn(row+1, col)){
            output.add(Directions.DOWN);
        }
        if(canStepOn(row-1, col)){
            output.add(Directions.UP);
        }
        if(canStepOn(row, col+1)){
            output.add(Directions.RIGHT);
        }
        if(canStepOn(row, col-1)){
            output.add(Directions.LEFT);
        }
        return output;
    }
    /**
     * Valida que una posición se encuentre dentro de los límites del laberinto.
     * @param row Fila a validar
     * @param col Columna a validar
     * @return true si las coordenadas son válidas, false si están fuera de rango
     */
    private boolean posOK(int row, int col){
        return (row>=0 && row<this.nRows &&col>=0 && col<this.nCols);
    }
    /**
     * Comprueba si una celda no tiene ocupantes en el laberinto.
     * @param row Fila a revisar
     * @param col Columna a revisar
     * @return true si la celda está libre, false si está ocupada
     */
    private boolean emptyPos(int row, int col){
        return labyrinth[row][col]==EMPTY_CHAR;
    }
    /**
     * Determina si existe un monstruo en una celda específica.
     * @param row Fila a examinar
     * @param col Columna a examinar
     * @return true si hay monstruo presente, false en caso contrario
     */
    private boolean monsterPos(int row, int col){
        return labyrinth[row][col]==MONSTER_CHAR;
    }
    /**
     * Identifica si una casilla corresponde a la salida del laberinto.
     * @param row Fila a verificar
     * @param col Columna a verificar
     * @return true si es la casilla de salida, false si no lo es
     */
    private boolean exitPos(int row, int col){
        return labyrinth[row][col]==EXIT_CHAR;
    }
    /**
     * Detecta si una celda se encuentra en confrontación activa.
     * @param row Fila a consultar
     * @param col Columna a consultar
     * @return true si hay combate en progreso, false de lo contrario
     */
    private boolean combatPos(int row, int col){
        return labyrinth[row][col]==COMBAT_CHAR;
    }
    /**
     * Valida que una posición sea accesible en el laberinto (contiene monstruo, 
     * está vacía o es la salida).
     * @param row Fila a evaluar
     * @param col Columna a evaluar
     * @return true si la posición es transitable, false si es un obstáculo
     */
    private boolean canStepOn(int row, int col){
        return posOK(row, col)&&(emptyPos(row, col)||monsterPos(row, col)||exitPos(row, col));
    }
    /**
     * Actualiza el estado de la casilla anterior tras un movimiento, restableciendo
     * su condición según contenga un monstruo o esté libre.
     * @param row Fila anterior
     * @param col Columna anterior
     */
    private void updateOldPos(int row, int col){
        if (posOK(row, col)){
            if (combatPos(row, col)){
                labyrinth[row][col]=MONSTER_CHAR;
            }else{
                labyrinth[row][col]=EMPTY_CHAR;
            }
        }
    }
    /**
     * Convierte coordenadas y dirección en una nueva posición desplazada una unidad.
     * @param row Fila de partida
     * @param col Columna de partida
     * @param direction Dirección del desplazamiento
     * @return Array con las nuevas coordenadas
     */
    private int[] dir2Pos(int row, int col, Directions direction){
        int[] pos = new int[]{row, col};
        switch (direction){
            case RIGHT:
                pos[1]++;
                break;
            case LEFT:
                pos[1]--;
                break;
            case UP:
                pos[0]--;
                break;
            case DOWN:
                pos[0]++;
                break;
        }
        return pos;
    }
    /**
     * Genera una posición aleatoria dentro del laberinto garantizando que esté vacía.
     * @return Array con coordenadas de una celda disponible
     */
    private int[] randomEmptyPos(){        
        int row;
        int col;
        int[] pos = new int[2];

        do {
            row = Dice.randomPos(nRows);
            col = Dice.randomPos(nCols);
        } while (!emptyPos(row, col));
        pos[ROW] = row;
        pos[COL] = col;
        return pos;
    }
    /**
     * Repositiona un jugador hacia nuevas coordenadas, actualizando el estado de ambas
     * casillas (anterior y nueva). Retorna el monstruo en la nueva ubicación si hay combate.
     *
     * Solo modifica la casilla anterior si el jugador identificado coincide con el almacenado.
     *
     * @param oldRow Fila anterior del jugador
     * @param oldCol Columna anterior del jugador
     * @param row Nueva fila del jugador
     * @param col Nueva columna del jugador
     * @param player Jugador a desplazar
     * @return Monstruo de la nueva casilla si hay enfrentamiento
     */
    private Monster putPlayer2D(int oldRow, int oldCol, int row, int col, Player player){
        Monster output = null;
        if (canStepOn(row, col)){
            if (posOK(oldRow, oldCol)){
                Player p = players[oldRow][oldCol];
                
                if (p == player){
                    updateOldPos(oldRow, oldCol);
                    players[oldRow][oldCol]=null;
                }
            }
            //Si hay un monstruo se actualiza a combate y se devuelve el monstruo
            if (monsterPos(row, col)){
                labyrinth[row][col]=COMBAT_CHAR;
                output=monsters[row][col];
            }else{ //Si no hay monstruo solo se pone el jugador en la casilla
                char number = player.getNumber();
                labyrinth[row][col]=number;
            }
            players[row][col]=player;
            player.setPos(row, col); 
        }
        return output;
    }
    /**
     * Reemplaza un jugador existente por su versión FuzzyPlayer en su ubicación actual.
     * @param other Instancia de FuzzyPlayer que ocupará el lugar del jugador actual
     */
    public void transformToFuzzy (FuzzyPlayer other){
        int row = other.getRow();
        int col = other.getCol();
        //aseguramos que esta creado correctamente a partir del player
        if (this.players[row][col].getNumber()==other.getNumber()){
            this.players[row][col]=other;
        }
    }
    
}
