# Team Project: Last Of Us
```python
teamMembers =       ("Sam Gunner", "Rana AL Badrani ", # @sjg745, @rxa792
                    "George Wellington ", "Tayyab Hussain", # @gcw737, @txh776
                    "Peiheng Li ", "Osanne Gbayere"); # @pxl742, @ocg785

teachingAssistant = "Thomas Goodman";
```
[TODO: LastOfUs](https://trello.com/invite/b/bn8zOmRW/4a027c272c65bcfe2c4aa95e10eb925e/lastofus)
## The Project : "Fortress Combat"
Our Project is a video game in Java, based on Tayyab's card game: "Fortress Combat"
The main requirements for our task are:
* Competitive Play
* Networking
* AI Controlled Opponents
* Music
* Graphics and a UI

Fortress Combat is a turn based card game where the objective is to reduce your
opponents "Castle Health" to zero before yours is. The basic game (1v1) revolves
purely around this, with cards such as :

* Soldiers - Protect your castle from attack, acting as a line of defense, have
specific abilities, and also having the ability to attack your opponent directly
if the situation allows.
* Mages - Have levels that progress as the game goes on, have specific abilities
for each level and can attack once they reach their maximum level, level 3.
* Tricksters - Effect cards that can be triggered when your enemy makes a move.
(Except in cases of Tricksters with special abilities, which do not need to
wait for opponents)
* Decrees - cards with powerful multi-turn effects that destroy
themselves at the end of their effect. Can impact the entire field of play.
* Castles - a players main card. You can only have one of these at a time during
a game - these represent your health, when they are destroyed you lose the
battle.

## Some Initial Ideas, Art and Similar Projects

#### Hearthstone

![Heathstone's Acclaimed UI](https://inanage.files.wordpress.com/2013/08/hearthstone_ui.jpg)

#### YGOPRO - Online Version of the TCG "Yu-Gi-Oh!"
![ygopro](https://www.ygopro.co/portals/0/Images/versions/1034beta.jpg)

#### Magic: The Gathering Arena
![Magic : Arena](https://www.destructoid.com//ul/528175-AA.jpg)

### Glossary

* **Ability** – The text given on mages or soldiers
* **Action** - Anything done by a player - drawing, summoning, declaring an
attack, etc.
* **Ally** – You and your 2v2 partner.
* **Cancel** – completely negate either an ability or attacks depending on
what is specified.
* **Enemy** – Your opponent(s)
* **Field** - Ability that applies constantly while the card remains on the field.
* **In Play**- Ability that can be activated once during each of your turns.
* **On play** - Ability that activates as soon as a card is played.
* **Stun** – A stunned soldier or mage cannot attack or use abilities until the
end of the owner’s turn.
* **On Lvl** - The instant that a mage levels up, they must use the given
ability.
* **Special** - An ability that can activate during either player’s turns (These
have the same activation windows as Tricksters).
