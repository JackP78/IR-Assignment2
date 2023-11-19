package ie.tcd.ir.grouptwelve;

public class QueryResult {


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    private int rank;

    private float score;

    private String runName= "Standard";

    private int queryId;

    private String documentId;

    public QueryResult(int queryId, String documentId, int rank, float score, String runName) {
        this.rank = rank;
        this.score = score;
        this.runName = runName;
        this.queryId = queryId;
        this.documentId = documentId;

    }

    @Override
    public String toString() {
        return Integer.toString(queryId) + "\t"
                + "Q0 \t"
                + documentId + "\t"
                + Integer.toString(rank) + "\t"
                + Float.toString(score) + "\t"
                + runName;
    }

}
