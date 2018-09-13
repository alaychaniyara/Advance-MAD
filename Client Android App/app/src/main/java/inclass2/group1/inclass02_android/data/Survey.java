package inclass2.group1.inclass02_android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Survey implements Serializable {
    ArrayList<SurveyInfo> surveys;

    public Survey() {
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveys=" + surveys +
                '}';
    }

    public ArrayList<SurveyInfo> getSurveys() {
        return surveys;
    }

    public void setSurveys(ArrayList<SurveyInfo> surveys) {
        this.surveys = surveys;
    }
}
