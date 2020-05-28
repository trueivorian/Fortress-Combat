package utils;

import gamelogic.Card;
import gamelogic.CardType;

public class Cost {
    CardType ct;
    int num;
    int level;
    int id;

    public CardType getCt() {
        return ct;
    }

    public void setCt(CardType ct) {
        this.ct = ct;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cost(CardType ct, int num, int level, int id){
        this.ct = ct;
        this.num = num;
        this.level = level;
        this.id = id;
    }
    public Cost(CardType ct, int num, int level){
        this.ct = ct;
        this.num = num;
        this.level = level;
    }
    public Cost(CardType ct, int num){
        this.ct = ct;
        this.num = num;
    }

    /**
     * Returns true if the supplied card satisfies this cost.
     * @param card
     * @return
     */
    public boolean satisfies(Card card){
        if (card.getType() == ct){
            return true;
        }
        else{
            return false;
        }

    }
}
