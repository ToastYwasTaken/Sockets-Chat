package pgdp.net;

import java.util.*;
import java.util.stream.Collectors;

public final class HttpRequest {
    public String request;
    HttpMethod method;

    public HttpRequest(String request){
        this.request = request;
        List<String> strlist = Arrays.stream(request.split(" ")).collect(Collectors.toList());
        if(strlist.size() != 3 || strlist.get(0).isEmpty()|| strlist.get(1).isEmpty()){
            throw new NumberFormatException ("The request format is false. Expected: <request-type> <path>.");
        }
    }

    public HttpMethod getMethod(){
        return HttpMethod.valueOf(Arrays.stream(request.split(" ")).collect(Collectors.toList()).get(0));
    }

    public String getPath(){
        return Arrays.stream(request.split(" ")).collect(Collectors.toList()).get(1);
    }

    public Map<String, String> getParameters(){
        return Arrays.stream(getPath().split("\\?")[1].split("&")).map(x -> x.split("=")).collect(Collectors.toMap(y -> y[0], y -> y[1]));
    }
}
