package com.dfsdk.ocr.scan.network.response;


public class DFNetworkBaseModule {
    private String status;
    private String reason;
    private String requestId;
    private String results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "DFNetworkBaseModule{" +
                "status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", requestId='" + requestId + '\'' +
                ", results='" + results + '\'' +
                '}';
    }
}
