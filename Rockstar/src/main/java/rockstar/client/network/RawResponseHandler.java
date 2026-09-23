package rockstar.client.network;


import rockstar.client.*;
import java.io.IOException;
import javax.annotation.Nonnull;

public class RawResponseHandler implements ResponseHandler<RockstarHttpResponse> {
   @Override
   public RockstarHttpResponse handle(@Nonnull RockstarHttpResponse localValue1) throws IOException {
      if (localValue1.internalMethod00588() >= 300) {
         throw new HttpRequestException(localValue1);
      } else {
         return localValue1;
      }
   }
}
