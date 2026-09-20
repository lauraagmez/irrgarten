
package irrgarten;


public class TestP1 {
    static void pruebaWeapon (){
        Weapon w = new Weapon(10.0f, 3);
        System.out.println("Instancia de la clase Weapon " + w.toString());
        for (int i=0; i<5; i++){
            System.out.println("Metodo attack: " + w.toString()+" "+ w.attack());
            if (w.discard()) System.out.println("Metodo discard: se descarta");
            else System.out.println("Metodo discard: no se descarta");
        }
    }
    static void pruebaShield (){
        Shield s = new Shield(5.3f, 5);
        System.out.println("Instancia de la clase Shield " + s.toString());
        for (int i=0; i<5; i++){
            System.out.println("Metodo aprotect: " + s.toString()+" "+ s.protect());
            if (s.discard()) System.out.println("Metodo discard: se descarta");
            else System.out.println("Metodo discard: no se descarta");
        }
    }
    static void pruebaDice (){
        for (int i=0; i<100; i++){
            //System.out.println("Uses= "+i);
            if (Dice.discardElement(2))
                System.out.println("Se descarta");
            else 
                System.out.println("No se descarta");
        }
    }
    static void pruebaEnumerados(){

        System.out.println("Movimiento: " + Directions.LEFT);
        System.out.println("Orientacion: " + Orientation.VERTICAL);
        System.out.println("Personaje: " + GameCharacter.PLAYER);    
    }
    /*public static void main(String[] args) {
        
        System.out.println("PRUEBA DE TODOS LOS METODOS DE LAS CLASES");
        pruebaWeapon();
        pruebaShield();
        System.out.println("PRUEBA DE LOS ENUMERADOS");
        pruebaEnumerados();
        System.out.println("PRUEBA DICE CON BUCLE DE 100 ITERACIONES");
        pruebaDice();


    }*/
}

