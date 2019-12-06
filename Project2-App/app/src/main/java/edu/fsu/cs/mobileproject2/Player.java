package edu.fsu.cs.mobileproject2;

import java.util.Random;

public class Player {
//    const int TOPHALF = 6;
//const int DICE = 5;
    Random rand;
    private String name;
    private int scoreForNum[];
    private int threeKind;
    private int fourKind;
    private int fullHouse;
    private int smallStraight;
    private int largeStraight;
    private int yahtzee;
    private int chance;
    private int upperTotal;
    private int lowerTotal;
    private int total;
    private boolean oneToSix[]; //bool for if already filled out on sheet
    private boolean ThreeKind;
    private boolean FourKind;
    private boolean FullHouse;
    private boolean SmallStraight;
    private boolean LargeStraight;
    private boolean Yahtzee;
    private boolean LowerTotal;
    private boolean UpperTotal;
    private boolean Chance;
    private boolean Total;
    private int die[];
    private int extraYahtzee = 0;
    private boolean ExtraYahtzee = false;


    public Player(){
        scoreForNum = new int[6];
        oneToSix = new boolean[6];
        die = new int[5];

        scoreForNum[0] = 0;
        scoreForNum[1] = 0;
        scoreForNum[2] = 0;
        scoreForNum[3] = 0;
        scoreForNum[4] = 0;
        scoreForNum[5] = 0;
        threeKind = 0;
        fourKind = 0;
        fullHouse = 0;
        smallStraight = 0;
        largeStraight = 0;
        yahtzee = 0;
        chance = 0;
        upperTotal = 0;
        lowerTotal = 0;
        total = 0;

        for(int i = 0; i < 6; i++)
        {
            oneToSix[i] = false;
        }

        ThreeKind = false;
        FourKind = false;
        FullHouse = false;
        SmallStraight = false;
        LargeStraight = false;
        LowerTotal = true;
        UpperTotal = true;
        Total = false;
    }

    public Player(String name, int[] scoreForNum, int threeKind, int fourKind, int fullHouse, int smallStraight, int largeStraight, int yahtzee, int chance, int upperTotal, int lowerTotal, int total, boolean[] oneToSix, boolean threeKind1, boolean fourKind1, boolean fullHouse1, boolean smallStraight1, boolean largeStraight1, boolean yahtzee1, boolean lowerTotal1, boolean upperTotal1, boolean chance1, boolean total1, int[] die) {
        this.name = name;
        this.scoreForNum = scoreForNum;
        this.threeKind = threeKind;
        this.fourKind = fourKind;
        this.fullHouse = fullHouse;
        this.smallStraight = smallStraight;
        this.largeStraight = largeStraight;
        this.yahtzee = yahtzee;
        this.chance = chance;
        this.upperTotal = upperTotal;
        this.lowerTotal = lowerTotal;
        this.total = total;
        this.oneToSix = oneToSix;
        ThreeKind = threeKind1;
        FourKind = fourKind1;
        FullHouse = fullHouse1;
        SmallStraight = smallStraight1;
        LargeStraight = largeStraight1;
        Yahtzee = yahtzee1;
        LowerTotal = lowerTotal1;
        UpperTotal = upperTotal1;
        Chance = chance1;
        Total = total1;
        this.die = die;
    }

    public String getName() {
        return name;
    }

    public int[] getScoreForNum() {
        return scoreForNum;
    }

    public int getThreeKind() {
        return threeKind;
    }

    public int getFourKind() {
        return fourKind;
    }

    public int getFullHouse() {
        return fullHouse;
    }

    public int getSmallStraight() {
        return smallStraight;
    }

    public int getLargeStraight() {
        return largeStraight;
    }

    public int getYahtzee() {
        return yahtzee;
    }

    public int getChance() {
        return chance;
    }

    public int getUpperTotal() {
        return upperTotal;
    }

    public int getLowerTotal() {
        return lowerTotal;
    }

    public int getTotal() {
        return total;
    }

    public boolean[] getOneToSix() {
        return oneToSix;
    }

    public boolean isThreeKind() {
        int count3;

        //if they already have 3 of a kind filled in, return false
        if(ThreeKind)
        {
           return false;
        }

        //check to see if there are 3 of a kind in their die
        for(int i = 0; i < 5; i++)
        {
            count3 = 0;

            for(int p = 0; p < 5; p++)
            {
                if(die[i] == die[p])
                {
                    count3++;
                }

                if(count3 == 3)
                {
                    threeKind = die[i]*3;
                    ThreeKind = true;
                    return true;
                }
            }
        }
        return false;
    }

    //same as 3 of a kind function except check for 4 of a kind
    public boolean isFourKind() {
        int count4;

        //if they already have 4 of a kind filled in, return false
        if(FourKind)
        {
            return false;
        }

        //check to see if there are 4 of a kind in their die
        for(int i = 0; i < 5; i++)
        {
            count4 = 0;

            for(int p = 0; p < 5; p++)
            {
                if(die[i] == die[p])
                {
                    count4++;
                }

                if(count4 == 4)
                {
                    threeKind = die[i]*4;
                    FourKind = true;
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isFullHouse(){
        //return false if they already have a full house
        if(FullHouse)
        {
            return false;
        }

        //sorting the die from smallest to largest
        SortDie();

        if(die[0] == die[1] && die[2] == die[3] && die[3] == die[4]){
            FullHouse = true;
            fullHouse = 25;
            return true;
        }

        else if(die[0] == die[1] && die[1] == die[2] && die[3] == die[4]){
            FullHouse = true;
            fullHouse = 25;
            return true;
        }

        else {
            return false;
        }
    }

    public boolean isSmallStraight() {
        int count = 0;

        if(SmallStraight){
            return false;
        }

        //sorting die from smallest to largest
        SortDie();

        //checking for straight, whether it be small or large (since large can be a small straight)
        for(int i = 0; i < 4; i++)
        {
            if(die[i] == die[i+1] - 1)
            {
                count++;
            }
        }

        if(count == 3 || count == 4)
        {
            SmallStraight = true;
            smallStraight = 30;
            return true;
        }
        else
        {
            count = 0;
        }

        //checking again in case last one was like "2,2,3,4,5"
        //since even though that was a straight if would have evaluated false in the last check
        for(int i = 1; i < 4; i++)
        {
            if(die[i] == die[i+1] - 1)
            {
                count++;
            }
        }

        if(count == 3 || count == 4)
        {
            SmallStraight = true;
            smallStraight = 30;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isLargeStraight() {
        int count = 0;

        if(LargeStraight){
            return false;
        }

        //sorting die from smallest to largest
        SortDie();

        //checking for large straight (ex. 1,2,3,4,5)
        for(int i = 0; i < 4; i++)
        {
            if(die[i] == die[i+1] - 1)
            {
                count++;
            }
        }

        if(count == 4)
        {
            LargeStraight = true;
            largeStraight = 30;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isYahtzee() {
        //makes sure all die are equal
        if(die[0] == die[1] && die[0] == die[2] && die[0] == die[3] && die[0] == die[4])
        {
            if(Yahtzee){
                extraYahtzee += 100;
                ExtraYahtzee = true;
                return true;
            }
            Yahtzee = true;
            yahtzee = 50;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isChance() {
        if(Chance)
        {
            return false;
        }

        for(int i = 0; i < 5; i++)
        {
            chance += die[i];
        }

        Chance = true;
        return true;
    }

    public boolean isTotal() {
        if(Total){
            return false;
        }
        else{
            return true;
        }
    }

    public int[] getDie() {
        return die;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScoreForNum(int[] scoreForNum) {
        this.scoreForNum = scoreForNum;
    }

    public void setThreeKind(int threeKind) {
        this.threeKind = threeKind;
    }

    public void setFourKind(int fourKind) {
        this.fourKind = fourKind;
    }

    public void setFullHouse(int fullHouse) {
        this.fullHouse = fullHouse;
    }

    public void setSmallStraight(int smallStraight) {
        this.smallStraight = smallStraight;
    }

    public void setLargeStraight(int largeStraight) {
        this.largeStraight = largeStraight;
    }

    public void setYahtzee(int yahtzee) {
        this.yahtzee = yahtzee;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setUpperTotal(int upperTotal) {
        this.upperTotal = upperTotal;
    }

    public void setLowerTotal(int lowerTotal) {
        this.lowerTotal = lowerTotal;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setOneToSix(boolean[] oneToSix) {
        this.oneToSix = oneToSix;
    }

    public void setThreeKind(boolean threeKind) {
        ThreeKind = threeKind;
    }

    public void setFourKind(boolean fourKind) {
        FourKind = fourKind;
    }

    public void setFullHouse(boolean fullHouse) {
        FullHouse = fullHouse;
    }

    public void setSmallStraight(boolean smallStraight) {
        SmallStraight = smallStraight;
    }

    public void setLargeStraight(boolean largeStraight) {
        LargeStraight = largeStraight;
    }

    public void setYahtzee(boolean yahtzee) {
        Yahtzee = yahtzee;
    }

    public void setLowerTotal(boolean lowerTotal) {
        LowerTotal = lowerTotal;
    }

    public void setUpperTotal(boolean upperTotal) {
        UpperTotal = upperTotal;
    }

    public void setChance(boolean chance) {
        Chance = chance;
    }

    public void setTotal(boolean total) {
        Total = total;
    }

    public void setDie(int[] die) {
        this.die = die;
    }

    public void InitializeDie() {
        rand = new Random();
        for(int i = 0; i < 5; i++)    //randomly assigning die number 1-6
        {
            die[i] = rand.nextInt(6) % 6 + 1;
        }
    }

    //users chooses which category to cross out
    public boolean crossOutOption(int crossThisOut){
        switch (crossThisOut){
            case 0:
                if(oneToSix[0]){
                    return false;
                }
                else{
                    oneToSix[0] = true;
                    scoreForNum[0] = 0;
                    return true;
                }
            case 1:
                if(oneToSix[1]){
                    return false;
                }
                else{
                    oneToSix[1] = true;
                    scoreForNum[1] = 0;
                    return true;
                }
            case 2:
                if(oneToSix[2]){
                    return false;
                }
                else{
                    oneToSix[2] = true;
                    scoreForNum[2] = 0;
                    return true;
                }

            case 3:
                if(oneToSix[3]){
                    return false;
                }
                else{
                    oneToSix[3] = true;
                    scoreForNum[3] = 0;
                    return true;
                }

            case 4:
                if(oneToSix[4]){
                    return false;
                }
                else{
                    oneToSix[4] = true;
                    scoreForNum[4] = 0;
                    return true;
                }

            case 5:
                if(oneToSix[5]){
                    return false;
                }
                else{
                    oneToSix[5] = true;
                    scoreForNum[5] = 0;
                    return true;
                }

            case 6:
                if(ThreeKind){
                    return false;
                }
                else{
                    ThreeKind = true;
                    threeKind = 0;
                    return true;
                }

            case 7:
                if(FourKind){
                    return false;
                }
                else{
                    FourKind = true;
                    fourKind = 0;
                    return true;
                }

            case 8:
                if(FullHouse){
                    return false;
                }
                else{
                    FullHouse = true;
                    fullHouse = 0;
                    return true;
                }

            case 9:
                if(SmallStraight){
                    return false;
                }
                else{
                    SmallStraight = true;
                    smallStraight = 0;
                    return true;
                }

            case 10:
                if(LargeStraight){
                    return false;
                }
                else{
                    LargeStraight = true;
                    largeStraight = 0;
                    return true;
                }

            case 11:
                if(Yahtzee){
                    return false;
                }
                else{
                    yahtzee = 0;
                    return true;
                }

            case 12:
                if(Chance){
                    return false;
                }
                else{
                    chance = 0;
                    return true;
                }

        }
        //if no number matches switch, return false...
        return false;
    }

    public boolean isOnes(){

        if(oneToSix[0]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 1){
                sum += 1;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[0] = sum;
            oneToSix[0] = true;
            return true;
        }
    }
    public boolean isTwos(){
        if(oneToSix[1]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 2){
                sum += 2;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[1] = sum;
            oneToSix[1] = true;
            return true;
        }
    }
    public boolean isThrees(){
        if(oneToSix[2]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 3){
                sum += 3;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[2] = sum;
            oneToSix[2] = true;
            return true;
        }
    }
    public boolean isFours(){
        if(oneToSix[3]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 4){
                sum += 4;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[3] = sum;
            oneToSix[3] = true;
            return true;
        }
    }
    public boolean isFives(){
        if(oneToSix[4]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 5){
                sum += 5;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[4] = sum;
            oneToSix[4] = true;
            return true;
        }
    }
    public boolean isSixes(){
        if(oneToSix[5]){
            return false;
        }

        int sum = 0;

        for(int i = 0; i < 5; i++){
            if(die[i] == 6){
                sum += 6;
            }
        }

        if(sum == 0){
            return false;
        }
        else{
            scoreForNum[5] = sum;
            oneToSix[5] = true;
            return true;
        }
    }
    //adds every score together
    public void TotalUp(){
        lowerTotal = 0;
        upperTotal = 0;

        for(int i = 0; i < 6; i++)
        {
            upperTotal += scoreForNum[i];
        }

        if(upperTotal >= 63)
        {
            total += 35;
        }

        lowerTotal = threeKind + fourKind + fullHouse + smallStraight + largeStraight + yahtzee + chance;

        total += upperTotal + lowerTotal;
    }

    public int ReturnTotal(){
        return total;
    }

    public void SortDie(){
        int sortTemp;

        //sorts die from smallest to largest
        for(int p = 0; p < 4; p++)
        {
            for(int i = 0; i < 4; i++)
            {
                if(die[i] > die[i+1])
                {
                    sortTemp = die[i];
                    die[i] = die[i+1];
                    die[i+1] = sortTemp;
                }
            }
        }
    }

    public void setOneDie(){
        die[0] = rand.nextInt(6) % 6 + 1;
    }
    public void setTwoDie(){
        die[1] = rand.nextInt(6) % 6 + 1;
    }
    public void setThreeDie(){
        die[2] = rand.nextInt(6) % 6 + 1;
    }
    public void setFourDie(){
        die[3] = rand.nextInt(6) % 6 + 1;
    }
    public void setFiveDie(){
        die[4] = rand.nextInt(6) % 6 + 1;
    }
    public int getOneDie(){
        return die[0];
    }
    public int getTwoDie(){
        return die[1];
    }
    public int getThreeDie(){
        return die[2];
    }
    public int getFourDie(){
        return die[3];
    }
    public int getFiveDie(){
        return die[4];
    }


    //do these.....
    public boolean Reroll(){
        return true;
    }



}
