
package irrgarten;

public class Monster extends LabyrinthCharacter{
    private static final int INITIAL_HEALTH=5;

    
    public Monster(String name, float intelligence, float strength){
        super(name, intelligence, strength, INITIAL_HEALTH);
    }
    
    @Override
    public float attack(){
        return Dice.intensity(this.getStrength());
    }
    @Override
    public boolean defend(float receivedAttack){
        boolean isDead = dead();
        if (!isDead){
            float defensiveEnergy=Dice.intensity(this.getIntelligence());
            if (defensiveEnergy<receivedAttack){
                this.gotWounded();
                isDead=this.dead();
            }
        }
        return isDead;
    }
   
    
}
