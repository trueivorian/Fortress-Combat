package ai;
import gamelogic.Card;
import gamelogic.Deck;
import graphics.UIController;
import graphics.exceptions.InvalidCardPosException;
import graphics.utils.CardEvent;
import graphics.utils.CardGraphicsObject;
import graphics.utils.CardGraphicsObjectDictionary;
import graphics.utils.EventType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import networking.exceptions.CardLogicException;
import utils.CardPos;
import utils.exceptions.CardEventException;

import java.util.HashMap;
import java.util.Optional;


public class AI  extends DeckCards implements Runnable {


    static boolean[] conditions;
    static int roundCounter = 0;
    static float maxRewardRule1 = 0;
    static float maxRewardRule2 = 0;
    static float maxRewardRule3 = 0;
    static float minRewardRule1 = 0;
    static float minRewardRule2 = 0;
    static float minRewardRule3 = 0;
    static float[] max_rewards = new float[3];
    static float[] min_rewards = new float[3];
    private boolean isRunning;
    static int  myCastle=0;
    static int theirCastle=0;


    protected static boolean[] Conditions() {
        conditions[0] = CardPos.MY_SOLDIER_1 != null;
        conditions[1] = CardPos.MY_SOLDIER_2 != null;

        conditions[4] = CardPos.MY_DECREE != null;
        conditions[5] = CardPos.MY_MAGE_1 != null;
        conditions[6] = CardPos.MY_MAGE_2 != null;

        conditions[8] = CardPos.THEIR_SOLDIER_1 != null;
        conditions[9] = CardPos.THEIR_SOLDIER_2 != null;
        conditions[10] = CardPos.THEIR_MAGE_1 != null;
        conditions[11] = CardPos.THEIR_MAGE_2 != null;

        conditions[13] = CardPos.MY_TRICKSTER_1 != null;
        conditions[14] = CardPos.MY_TRICKSTER_2 != null;
        return conditions;

    }

        protected  static void getCastleHealth(CardEvent e)
        {
            EventType eventType = e.getEventType();
            switch(eventType)
            {
                case MY_CASTLE_HEALTH:
                   myCastle=e.getValue();
                 break;

                case THEIR_CASTLE_HEALTH:

                theirCastle=e.getValue();
                    break;

            }
        }

    public static void main(String[]args)
    {
        AI aiInstance=new AI();
        Thread ai=new Thread(aiInstance);
        ai.start();
    }






    public AI() {
        DeckCards test=new DeckCards();
        run();
        isRunning = true;

    }

    private void executeAICardEvent(CardEvent e) throws CardEventException {
        // Gets the eventType associated with the cardEvent
        EventType eventType = e.getEventType();
        switch(eventType){
            case MULLIGAN_MSG:
                UIController.option(true);
                break;

            case TURN_START:

                getCastleHealth(e);
                rule1(e);
                rule2(e);
                rule3(e);

                break;
            case TARGET_CARD:

                e.getValidBoardRegions().get(0);
                break;



        }

    //rule1(e);


        if (e.getCard() != null) { // Executes events acting on card objects
            // Maintain Hashmaps
        }

    }



    protected static void attacks() {
        int didAttack = 0;

        if (conditions[7] && conditions[0] && conditions[8] && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_1, CardPos.THEIR_SOLDIER_1);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (conditions[7] && conditions[1] && conditions[8] && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_2, CardPos.THEIR_SOLDIER_1);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[0] && conditions[9] && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_1, CardPos.THEIR_SOLDIER_2);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (conditions[7] && conditions[1] && conditions[9] && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_2, CardPos.THEIR_SOLDIER_2);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[6] && (conditions[8] && conditions[9] == false) && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_2, CardPos.THEIR_CASTLE);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[5] && (conditions[8] && conditions[9] == false) && didAttack == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_1, CardPos.THEIR_CASTLE);
                didAttack++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        didAttack = 0;

    }

    protected static void levelUP() {
        int didAttack1 = 0;
        if (conditions[7] && conditions[5] && conditions[10] && roundCounter >= 2 && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_1, CardPos.THEIR_MAGE_1);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (conditions[7] && conditions[6] && conditions[10] && roundCounter >= 2 && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_2, CardPos.THEIR_MAGE_1);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[5] && conditions[11] && roundCounter >= 2 && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_1, CardPos.THEIR_MAGE_2);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (conditions[7] && conditions[6] && conditions[11] && roundCounter >= 2 && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_MAGE_2, CardPos.THEIR_MAGE_2);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[5] && roundCounter < 2) {
            try {
                UIController.levelMage(CardPos.MY_MAGE_1);
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[6] && roundCounter < 2) {
            try {
                UIController.levelMage(CardPos.MY_MAGE_2);
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[1] && (conditions[8] && conditions[9] == false) && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_2, CardPos.THEIR_CASTLE);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (conditions[7] && conditions[0] && (conditions[8] && conditions[9] == false) && didAttack1 == 0) {
            try {
                UIController.attack(CardPos.MY_SOLDIER_1, CardPos.THEIR_CASTLE);
                didAttack1++;
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        didAttack1 = 0;

    }
    private static void play(CardPos p1, CardPos p2){
        HashMap<CardPos, CardGraphicsObject> cardMap = CardGraphicsObjectDictionary.getAll();
        if((!cardMap.keySet().contains(p2))||(cardMap.get(p2) == null)){
            try {
                UIController.play(p1, p2);
            } catch (CardLogicException e) {
                e.printStackTrace();
            } catch (InvalidCardPosException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void activateTricksters() {
        if (conditions[3]) {

            try {
                UIController.activateTrickster(CardPos.MY_TRICKSTER_1);
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else if (conditions[12]) {
            try {
                UIController.activateTrickster(CardPos.MY_TRICKSTER_2);
            } catch (CardLogicException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    protected static void rule1(CardEvent e)
    {


        while((theirCastle!=0)||(myCastle)!=0)
        {
            while (e.getFlag()) {


                if ((max_rewards.length == 0 && min_rewards.length == 0)||(max_rewards.length == 3 && min_rewards.length == 3)) {

                    play(CardPos.MY_HAND_1,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_1,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_1,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_1,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_2,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_3,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_4,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_5,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_6,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_6,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_6,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_TRICKSTER_2);







                    float firstMin = myCastle;//
                    float firstMax = theirCastle;//
                    attacks();
                    attacks();
                    levelUP();
                    UIController.endTurn();


                    max_rewards[0] = maxRewardRule1 = firstMax - theirCastle;//
                    min_rewards[0] = minRewardRule1 = firstMin - myCastle;//


                } else {

                    maxMinRewards(e);

                }
            }


        }
    }

    protected static void rule2(CardEvent e)
    {
        while((theirCastle!=0)||(myCastle)!=0)
      {
          {
              while (e.getFlag())
              {


                  if (max_rewards.length == 0 && min_rewards.length == 0)
                  {

                      play(CardPos.MY_HAND_1, CardPos.MY_SOLDIER_2);
                      play(CardPos.MY_HAND_1, CardPos.MY_SOLDIER_1);
                      play(CardPos.MY_HAND_1, CardPos.MY_MAGE_2);
                      play(CardPos.MY_HAND_1, CardPos.MY_MAGE_1);
                      play(CardPos.MY_HAND_1, CardPos.MY_DECREE);
                      play(CardPos.MY_HAND_1, CardPos.MY_TRICKSTER_1);
                      play(CardPos.MY_HAND_1, CardPos.MY_TRICKSTER_2);
                      play(CardPos.MY_HAND_2, CardPos.MY_SOLDIER_2);
                      play(CardPos.MY_HAND_2, CardPos.MY_SOLDIER_1);
                      play(CardPos.MY_HAND_2, CardPos.MY_MAGE_2);
                      play(CardPos.MY_HAND_2, CardPos.MY_MAGE_1);
                      play(CardPos.MY_HAND_2, CardPos.MY_DECREE);
                      play(CardPos.MY_HAND_2, CardPos.MY_TRICKSTER_1);
                      play(CardPos.MY_HAND_2, CardPos.MY_TRICKSTER_2);
                      play(CardPos.MY_HAND_3, CardPos.MY_SOLDIER_2);
                      play(CardPos.MY_HAND_3, CardPos.MY_SOLDIER_1);
                      play(CardPos.MY_HAND_3, CardPos.MY_MAGE_2);
                      play(CardPos.MY_HAND_3, CardPos.MY_MAGE_1);
                      play(CardPos.MY_HAND_3, CardPos.MY_DECREE);
                      play(CardPos.MY_HAND_3, CardPos.MY_TRICKSTER_1);
                      play(CardPos.MY_HAND_3, CardPos.MY_TRICKSTER_2);
                      play(CardPos.MY_HAND_4, CardPos.MY_SOLDIER_2);
                      play(CardPos.MY_HAND_4, CardPos.MY_SOLDIER_1);
                      play(CardPos.MY_HAND_4, CardPos.MY_MAGE_2);
                      play(CardPos.MY_HAND_4, CardPos.MY_MAGE_1);
                      play(CardPos.MY_HAND_4, CardPos.MY_DECREE);
                      play(CardPos.MY_HAND_4, CardPos.MY_TRICKSTER_1);
                      play(CardPos.MY_HAND_4, CardPos.MY_TRICKSTER_2);
                      play(CardPos.MY_HAND_5, CardPos.MY_SOLDIER_2);
                      play(CardPos.MY_HAND_6, CardPos.MY_SOLDIER_1);
                      play(CardPos.MY_HAND_6, CardPos.MY_MAGE_2);
                      play(CardPos.MY_HAND_6, CardPos.MY_MAGE_1);
                      play(CardPos.MY_HAND_6, CardPos.MY_DECREE);
                      play(CardPos.MY_HAND_6, CardPos.MY_TRICKSTER_1);
                      play(CardPos.MY_HAND_6, CardPos.MY_TRICKSTER_2);

                      float firstMin = myCastle;//
                      float firstMax = theirCastle;//
                      attacks();
                      levelUP();
                      levelUP();
                      attacks();
                      attacks();
                      UIController.endTurn();
                      activateTricksters();


                      max_rewards[0] = maxRewardRule1 = firstMax - theirCastle;//
                      min_rewards[0] = minRewardRule1 = firstMin - myCastle;//


                  } else
                      {

                         maxMinRewards(e);

                      }
              }


          }
    }}


    protected static void rule3(CardEvent e)
    {
            while((theirCastle!=0)||(myCastle)!=0)

        {
            while (e.getFlag())
            {


                if (max_rewards.length == 0 && min_rewards.length == 0)
                {
                    play(CardPos.MY_HAND_1,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_1,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_1,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_1,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_1,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_2,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_2,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_2,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_3,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_3,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_3,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_4,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_4,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_4,CardPos.MY_TRICKSTER_2);
                    play(CardPos.MY_HAND_5,CardPos.MY_SOLDIER_2);
                    play(CardPos.MY_HAND_6,CardPos.MY_SOLDIER_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_MAGE_2);
                    play(CardPos.MY_HAND_6,CardPos.MY_MAGE_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_DECREE);
                    play(CardPos.MY_HAND_6,CardPos.MY_TRICKSTER_1);
                    play(CardPos.MY_HAND_6,CardPos.MY_TRICKSTER_2);

                    float firstMin = myCastle;//
                    float firstMax = theirCastle;//

                    attacks();
                    attacks();
                    levelUP();
                    levelUP();
                    attacks();
                    levelUP();
                    attacks();
                    UIController.endTurn();
                    activateTricksters();
                    activateTricksters();


                   max_rewards[0] = maxRewardRule1 = firstMax - theirCastle;//
                    min_rewards[0] = minRewardRule1 = firstMin - myCastle;//


                } else
                    {

                    maxMinRewards(e);

                }
            }


        }
    }


    protected static void maxMinRewards(CardEvent e)
        {
            while ((theirCastle != 0) || (myCastle) != 0)
            {
                {
                    if (e.getFlag())
                    {
                        if (min_rewards[0] <= min_rewards[1] && min_rewards[0] <= min_rewards[2])
                        {
                            rule1(e);

                        } else if (min_rewards[1] <= min_rewards[0] && min_rewards[1] <= min_rewards[2])
                        {
                            rule2(e);

                        } else
                            {
                            rule3(e);

                        }
                    } else
                        {
                        if (max_rewards[0] >= max_rewards[1] && max_rewards[0] >= max_rewards[2])
                        {
                            rule1(e);

                        } else if (max_rewards[1] >= max_rewards[0] && max_rewards[1] >= max_rewards[2])
                        {
                            rule2(e);

                        } else
                            {
                            rule3(e);

                        }

                    }
                }

            }
        }






    public void close ()
        {
            isRunning = false;
        }

        public void run ()
        {

            while (isRunning)
            {
                // Thread Operations
                try
                {
                    executeAICardEvent(UIController.popEvent());
                } catch (CardEventException e)
                {
                    e.printStackTrace();
                }

            }
        }


    }
