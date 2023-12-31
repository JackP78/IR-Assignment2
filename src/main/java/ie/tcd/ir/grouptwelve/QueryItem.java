package ie.tcd.ir.grouptwelve;

public class QueryItem {

    public QueryItem(int id, String title, String description, String relevantNarrative, String irrelevant) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.narrative = relevantNarrative;
        this.irrelevant = irrelevant;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIrrelevant() {
        return irrelevant;
    }

    private String description;

    private String narrative;

    private String title;

    private String irrelevant;
}
