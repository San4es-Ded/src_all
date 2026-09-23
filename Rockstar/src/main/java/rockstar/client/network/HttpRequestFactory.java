package rockstar.client.network;


import rockstar.client.*;
import java.net.MalformedURLException;
import java.net.URL;
import rockstar.client.network.HttpDeleteRequest;
import rockstar.client.network.HttpGetRequest;
import rockstar.client.network.HttpHeadRequest;
import rockstar.client.network.HttpPostRequest;
import rockstar.client.network.HttpPutRequest;
import rockstar.client.network.CustomHttpRequest;
import rockstar.client.network.RockstarHttpRequest;

public interface HttpRequestFactory {
    default public <T extends RockstarHttpRequest> T internalMethod05303(T t) {
        return t;
    }

    default public HttpGetRequest internalMethod00681(String string) throws MalformedURLException {
        return this.internalMethod05303(new HttpGetRequest(string));
    }

    default public HttpGetRequest internalMethod03404(URL uRL) {
        return this.internalMethod05303(new HttpGetRequest(uRL));
    }

    default public HttpHeadRequest internalMethod00682(String string) throws MalformedURLException {
        return this.internalMethod05303(new HttpHeadRequest(string));
    }

    default public HttpHeadRequest internalMethod03405(URL uRL) {
        return this.internalMethod05303(new HttpHeadRequest(uRL));
    }

    default public HttpDeleteRequest internalMethod00680(String string) throws MalformedURLException {
        return this.internalMethod05303(new HttpDeleteRequest(string));
    }

    default public HttpDeleteRequest internalMethod03403(URL uRL) {
        return this.internalMethod05303(new HttpDeleteRequest(uRL));
    }

    default public HttpPostRequest internalMethod00683(String string) throws MalformedURLException {
        return this.internalMethod05303(new HttpPostRequest(string));
    }

    default public HttpPostRequest internalMethod03406(URL uRL) {
        return this.internalMethod05303(new HttpPostRequest(uRL));
    }

    default public HttpPutRequest internalMethod00686(String string) throws MalformedURLException {
        return this.internalMethod05303(new HttpPutRequest(string));
    }

    default public HttpPutRequest internalMethod03409(URL uRL) {
        return this.internalMethod05303(new HttpPutRequest(uRL));
    }

    default public RockstarHttpRequest internalMethod01538(String string, String string2) throws MalformedURLException {
        return this.internalMethod05303(new RockstarHttpRequest(string, string2));
    }

    default public RockstarHttpRequest internalMethod01409(String string, URL uRL) {
        return this.internalMethod05303(new RockstarHttpRequest(string, uRL));
    }

    default public CustomHttpRequest internalMethod01537(String string, String string2) throws MalformedURLException {
        return this.internalMethod05303(new CustomHttpRequest(string, string2));
    }

    default public CustomHttpRequest internalMethod01408(String string, URL uRL) {
        return this.internalMethod05303(new CustomHttpRequest(string, uRL));
    }
}

