package pgdp.net;

public final class HttpResponse {
    public HttpStatus status;
    public String responseBody;

    public HttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.responseBody = body;
    }

    @Override
    public String toString(){
        return "HTTP/1.1 " + status.getCode() + " " + status.getText() + "\r\n";
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
