package kr.osam2020.sum.Model;

public class IndexExpert {
    private String id;
    private String language_position;
    private String language_edu;
    private String language_career;
    private String language_performance;
    private String combat_position;
    private String combat_edu;
    private String combat_career;
    private String combat_performance;
    private String computer_position;
    private String computer_edu;
    private String computer_career;
    private String computer_performance;
    private String admin_position;
    private String admin_edu;
    private String admin_career;
    private String admin_performance;
    private String law_position;
    private String law_edu;
    private String law_career;
    private String law_performance;

    private int[] vector;

    public IndexExpert(String id, String language_position, String language_edu, String language_career, String language_performance, String combat_position, String combat_edu, String combat_career, String combat_performance, String computer_position, String computer_edu, String computer_career, String computer_performance, String admin_position, String admin_edu, String admin_career, String admin_performance, String law_position, String law_edu, String law_career, String law_performance) {
        this.id = id;
        this.language_position = language_position;
        this.language_edu = language_edu;
        this.language_career = language_career;
        this.language_performance = language_performance;
        this.combat_position = combat_position;
        this.combat_edu = combat_edu;
        this.combat_career = combat_career;
        this.combat_performance = combat_performance;
        this.computer_position = computer_position;
        this.computer_edu = computer_edu;
        this.computer_career = computer_career;
        this.computer_performance = computer_performance;
        this.admin_position = admin_position;
        this.admin_edu = admin_edu;
        this.admin_career = admin_career;
        this.admin_performance = admin_performance;
        this.law_position = law_position;
        this.law_edu = law_edu;
        this.law_career = law_career;
        this.law_performance = law_performance;
    }

    public IndexExpert() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage_position() {
        return language_position;
    }

    public void setLanguage_position(String language_position) {
        this.language_position = language_position;
    }

    public String getLanguage_edu() {
        return language_edu;
    }

    public void setLanguage_edu(String language_edu) {
        this.language_edu = language_edu;
    }

    public String getLanguage_career() {
        return language_career;
    }

    public void setLanguage_career(String language_career) {
        this.language_career = language_career;
    }

    public String getLanguage_performance() {
        return language_performance;
    }

    public void setLanguage_performance(String language_performance) {
        this.language_performance = language_performance;
    }

    public String getCombat_position() {
        return combat_position;
    }

    public void setCombat_position(String combat_position) {
        this.combat_position = combat_position;
    }

    public String getCombat_edu() {
        return combat_edu;
    }

    public void setCombat_edu(String combat_edu) {
        this.combat_edu = combat_edu;
    }

    public String getCombat_career() {
        return combat_career;
    }

    public void setCombat_career(String combat_career) {
        this.combat_career = combat_career;
    }

    public String getCombat_performance() {
        return combat_performance;
    }

    public void setCombat_performance(String combat_performance) {
        this.combat_performance = combat_performance;
    }

    public String getComputer_position() {
        return computer_position;
    }

    public void setComputer_position(String computer_position) {
        this.computer_position = computer_position;
    }

    public String getComputer_edu() {
        return computer_edu;
    }

    public void setComputer_edu(String computer_edu) {
        this.computer_edu = computer_edu;
    }

    public String getComputer_career() {
        return computer_career;
    }

    public void setComputer_career(String computer_career) {
        this.computer_career = computer_career;
    }

    public String getComputer_performance() {
        return computer_performance;
    }

    public void setComputer_performance(String computer_performance) {
        this.computer_performance = computer_performance;
    }

    public String getAdmin_position() {
        return admin_position;
    }

    public void setAdmin_position(String admin_position) {
        this.admin_position = admin_position;
    }

    public String getAdmin_edu() {
        return admin_edu;
    }

    public void setAdmin_edu(String admin_edu) {
        this.admin_edu = admin_edu;
    }

    public String getAdmin_career() {
        return admin_career;
    }

    public void setAdmin_career(String admin_career) {
        this.admin_career = admin_career;
    }

    public String getAdmin_performance() {
        return admin_performance;
    }

    public void setAdmin_performance(String admin_performance) {
        this.admin_performance = admin_performance;
    }

    public String getLaw_position() {
        return law_position;
    }

    public void setLaw_position(String law_position) {
        this.law_position = law_position;
    }

    public String getLaw_edu() {
        return law_edu;
    }

    public void setLaw_edu(String law_edu) {
        this.law_edu = law_edu;
    }

    public String getLaw_career() {
        return law_career;
    }

    public void setLaw_career(String law_career) {
        this.law_career = law_career;
    }

    public String getLaw_performance() {
        return law_performance;
    }

    public void setLaw_performance(String law_performance) {
        this.law_performance = law_performance;
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector() {
        vector = new int[20];
        vector[0] = Integer.parseInt(getLanguage_position());
        vector[1] = Integer.parseInt(getLanguage_edu());
        vector[2] = Integer.parseInt(getLanguage_career());
        vector[3] = Integer.parseInt(getLanguage_performance());
        vector[4] = Integer.parseInt(getCombat_position());
        vector[5] = Integer.parseInt(getCombat_edu());
        vector[6] = Integer.parseInt(getCombat_career());
        vector[7] = Integer.parseInt(getCombat_performance());
        vector[8] = Integer.parseInt(getComputer_position());
        vector[9] = Integer.parseInt(getComputer_edu());
        vector[10] = Integer.parseInt(getComputer_career());
        vector[11] = Integer.parseInt(getComputer_performance());
        vector[12] = Integer.parseInt(getAdmin_position());
        vector[13] = Integer.parseInt(getAdmin_edu());
        vector[14] = Integer.parseInt(getAdmin_career());
        vector[15] = Integer.parseInt(getAdmin_performance());
        vector[16] = Integer.parseInt(getLaw_position());
        vector[17] = Integer.parseInt(getLaw_edu());
        vector[18] = Integer.parseInt(getLaw_career());
        vector[19] = Integer.parseInt(getLaw_performance());
    }
}
