package sg.edu.rp.pd.wouldyourather;

public class Question {
    private String red;
    private String blue;
    private int votesRed;
    private int votesBlue;
    private String creator;

    public Question(String red, String blue, int votesRed, int votesBlue, String creator) {
        this.red = red;
        this.blue = blue;
        this.votesRed = votesRed;
        this.votesBlue = votesBlue;
        this.creator = creator;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public int getVotesRed() {
        return votesRed;
    }

    public void setVotesRed(int votesRed) {
        this.votesRed = votesRed;
    }

    public int getVotesBlue() {
        return votesBlue;
    }

    public void setVotesBlue(int votesBlue) {
        this.votesBlue = votesBlue;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
