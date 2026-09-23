package rockstar.client.internal.network;



import rockstar.client.network.*;
import rockstar.client.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class McoCompatibilityRequest extends HttpGetRequest implements ResponseHandler<String> {
   public McoCompatibilityRequest(String localValue1) throws MalformedURLException {
      super("https://" + localValue1 + "/mco/client/compatible");
      this.internalMethod01193("Accept", MediaTypes.internalField1788.internalMethod05675());
   }

   @Override
   public String handle(RockstarHttpResponse localValue1) throws IOException {
      if (localValue1.internalMethod00588() >= 300) {
         throw new HttpRequestException(localValue1);
      } else {
         return localValue1.internalMethod02509().internalMethod04320();
      }
   }
}
