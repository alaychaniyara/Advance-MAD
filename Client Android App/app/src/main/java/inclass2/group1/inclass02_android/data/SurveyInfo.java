package inclass2.group1.inclass02_android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyInfo implements Serializable {

    private int questionNo;
    private String question,choice0,choice1,choice2,choice3,choice4;
    private String userChoice;
    private int totalAnswered;

    @Override
    public String toString() {
        return "SurveyInfo{" +
                "questionNo=" + questionNo +
                ", question='" + question + '\'' +
                ", choice0='" + choice0 + '\'' +
                ", choice1='" + choice1 + '\'' +
                ", choice2='" + choice2 + '\'' +
                ", choice3='" + choice3 + '\'' +
                ", choice4='" + choice4 + '\'' +
                ", userChoice='" + userChoice + '\'' +
                ", totalAnswered=" + totalAnswered +
                '}';
    }

    public String getChoice0() {
        return choice0;
    }

    public void setChoice0(String choice0) {
        this.choice0 = choice0;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public SurveyInfo() {
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(String userChoice) {
        this.userChoice = userChoice;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }
}
